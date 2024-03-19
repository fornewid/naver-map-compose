package land.sungbin.navermap.token

import com.naver.maps.geometry.Coord

public interface HasCoord {
  public val coord: Coord? get() = null
  public val coords: List<Coord>? get() = null

  public companion object {
    public operator fun invoke(coord: Coord): HasCoord =
      object : HasCoord {
        override val coord = coord
      }

    public operator fun invoke(coords: List<Coord>): HasCoord =
      object : HasCoord {
        override val coords = coords
      }

    public operator fun invoke(vararg coords: Coord): HasCoord =
      object : HasCoord {
        override val coords = coords.asList()
      }
  }
}
