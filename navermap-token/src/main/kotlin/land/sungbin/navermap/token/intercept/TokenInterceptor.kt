package land.sungbin.navermap.token.intercept

public fun interface TokenInterceptor<T> {
  public fun intercept(input: T): T

  public companion object {
    public inline operator fun <T> invoke(crossinline block: (T) -> T): TokenInterceptor<T> =
      TokenInterceptor { token -> block(token) }
  }
}
