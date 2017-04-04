package pulse.common

/**
 * A type-class aimed to define a resource-disposing strategy for an abstract resource
 */
trait Managed[A] {

  /**
   * Release the specified instance
   */
  def close(instance: A): Unit
}
