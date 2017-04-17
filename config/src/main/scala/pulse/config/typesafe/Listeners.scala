package pulse.config.typesafe

import java.io.File
import java.nio.file._
import java.util.concurrent.{Executors, ThreadFactory}

import com.google.common.util.concurrent.ThreadFactoryBuilder
import com.typesafe.config.ConfigFactory
import fs2.{Scheduler, Strategy, Stream, Task}
import pulse.common._
import pulse.common.syntax._
import pulse.common.exceptions._

import scala.annotation.tailrec

object Listeners {

  private[typesafe] val defaultWatchingStrategy   = Strategy.fromExecutor(Executors.newSingleThreadExecutor())

  private[typesafe] val defaultSchedulingStrategy = Scheduler.fromScheduledExecutorService(Executors.newScheduledThreadPool(1))

  private[typesafe] val defaultTimeout = 10

  private[typesafe] implicit val manageableWatchKey = new Managed[WatchKey] {
    def close(instance: WatchKey): Unit = instance.reset()
  }

  def file (path: Path, timeout: Int = defaultTimeout)(implicit St: Strategy = defaultWatchingStrategy, Sc: Scheduler = defaultSchedulingStrategy) =
    if (unavailable(path)) Stream.fail(ConfigException(s"File '$path' either does not exist or cannot be read")) else {
      fs2.Stream.eval(parse(path)) ++ new FileWatcher(path, timeout).changes
    }

  private def unavailable (p: Path) = !(Files.exists(p) && Files.isRegularFile(p) && Files.isReadable(p))

  private def parse(p: Path) = Task.delay {
    ConfigFactory.parseFile(p.toFile)
  }

}

private[typesafe] final class FileWatcher(path: Path, timeout: Int)(implicit St: Strategy, Sc: Scheduler) {
  import concurrent.duration._
  import Listeners._
  import fs2.interop.cats._
  import Task._

  assert(Files.isRegularFile(path))
  assert(Files.isReadable(path))

  private val watcher   = FileSystems.getDefault.newWatchService()
  private val directory = path.getParent

  directory.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY)

  def changes: Stream[Task, Path] = Stream.eval(watch) ++ changes

  private def watch: Task[Path] = Task { watcher.take() }.flatMap {
    key => use(key) { k => watch(k) }.flatMap {
      case Some(p) => now(p)
      case None    => watch.schedule(timeout.seconds)
    }
  }

  private def watch(key: WatchKey): Task[Option[Path]] = delay { key.pollEvents() } flatMap {
    list => now(traverse(list.iterator()) {
        e =>
          Files.isSameFile(e.context(), path.getFileName)
      }
    )
  }

  @tailrec
  private def traverse (it: java.util.Iterator[WatchEvent[_]])(matcher: WatchEvent[Path] => Boolean): Option[Path] = if (it.hasNext) {
    val event = it.next().asInstanceOf[WatchEvent[Path]]
    if (matcher(event)) Option(event.context()) else traverse(it)(matcher)
  } else None


}
