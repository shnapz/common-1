package pulse.config
package syntax

trait Descriptors {

  private def map[T](descriptor: DescriptorValue) = new TypeDescriptor[T] {
    override val value: DescriptorValue = descriptor
  }

  implicit val stringDescriptor: TypeDescriptor[String] = map[String](Values.String)

  implicit val integerDescriptor: TypeDescriptor[Int]   = map[Int](Values.Int)

  implicit val doubleDescriptor: TypeDescriptor[Double] = map[Double](Values.Double)

  implicit val booleanDescriptor: TypeDescriptor[Boolean] = map[Boolean](Values.Boolean)

  implicit val longDescriptor: TypeDescriptor[Long] = map[Long](Values.Long)

  implicit val anyRefDescriptor: TypeDescriptor[AnyRef] = map[AnyRef](Values.AnyRef)

  implicit val stringListDescriptor: TypeDescriptor[Seq[String]] = map[Seq[String]](Values.StringList)

  implicit val integerListDescriptor: TypeDescriptor[Seq[Int]]   = map[Seq[Int]](Values.IntList)

  implicit val doubleListDescriptor: TypeDescriptor[Seq[Double]] = map[Seq[Double]](Values.DoubleList)

  implicit val booleanListDescriptor: TypeDescriptor[Seq[Boolean]] = map[Seq[Boolean]](Values.BooleanList)

  implicit val longListDescriptor: TypeDescriptor[Seq[Long]] = map[Seq[Long]](Values.LongList)

  implicit val anyRefListDescriptor: TypeDescriptor[Seq[AnyRef]] = map[Seq[AnyRef]](Values.AnyRefList)

}
