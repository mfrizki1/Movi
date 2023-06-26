package id.calocallo.movi.utils

enum class PlaceHolderImage(val type: String) {
    Default("default"), Profile("profile");

    override fun toString(): String {
        return type
    }
}