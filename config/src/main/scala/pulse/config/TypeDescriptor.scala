package pulse.config

/**
 * A descriptor of the type explicitly requested from the configuration
 */
trait TypeDescriptor[T] {

  val value: DescriptorValue

}

trait DescriptorValue extends Product with Serializable

object Values {
  case object String      extends DescriptorValue
  case object Int         extends DescriptorValue
  case object Double      extends DescriptorValue
  case object Boolean     extends DescriptorValue
  case object Long        extends DescriptorValue
  case object AnyRef      extends DescriptorValue
  case object StringList  extends DescriptorValue
  case object IntList     extends DescriptorValue
  case object DoubleList  extends DescriptorValue
  case object BooleanList extends DescriptorValue
  case object LongList    extends DescriptorValue
  case object AnyRefList  extends DescriptorValue
  case object Unknown     extends DescriptorValue
}

