package pulse.config
package syntax

trait Descriptors {

  private def map[T](descriptor: DescriptorValue) = new TypeDescriptor[T] {
    override val value: DescriptorValue = descriptor
  }

  implicit val stringDescriptor: TypeDescriptor[String] = map[String](Values.String)

  implicit val integerDescriptor: TypeDescriptor[Int]   = map[Int](Values.Int)

}
