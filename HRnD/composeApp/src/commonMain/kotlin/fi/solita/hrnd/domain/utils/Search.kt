package fi.solita.hrnd.domain.utils

/**
 * Finds the index of the float in the given sorted list that is closest to the specified target float.
 * If there are multiple floats with the same minimum difference, returns the index of the first occurrence.
 * @param list A list of floats sorted in ascending order.
 * @param target The float for which the closest index is to be found.
 * @return The index of the float in the sorted list that is closest to the target float.
 */
fun binaryClosestFloatSearch(list: List<Float>, target: Float): Int{
    var left = 0
    var right = list.size - 1
    var closestIndex = 0
    var minDiff = Float.MAX_VALUE

    while (left <= right) {
        val mid = (left + right) / 2
        val diff = kotlin.math.abs(list[mid] - target)

        if (diff < minDiff) {
            minDiff = diff
            closestIndex = mid
        }

        if (list[mid] < target)
            left = mid + 1
        else if (list[mid] > target)
            right = mid - 1
        else
            return mid // exact match found
    }

    return closestIndex
}