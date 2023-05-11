package com.fcenesiz.decision_tree
import kotlin.math.log2



fun branches(
    bestGainKey: String,
    dataset: MutableMap<String, MutableList<String>>,
): MutableMap<String, MutableMap<String, MutableList<String>>> {
    dataset[bestGainKey]?.let { data ->
        dataset.remove(bestGainKey)

        val map: MutableMap<String, MutableMap<String, MutableList<String>>> = mutableMapOf()
        groupByValue(data).forEach { entry ->
            dataset.forEach {
                map[entry.key] = map[entry.key] ?: mutableMapOf()
                map[entry.key]?.put(it.key, includeIndexes(it.value, entry.value))
            }
        }
        return map
    }
    return mutableMapOf()
}

fun includeIndexes(list: MutableList<String>, indexes: List<Int>): MutableList<String> {
    return indexes.map { list[it] }.toMutableList()
}

/**
 * Entropy
 * Info(D) = -∑[pI * log2(pI)]
 */
fun info(left: Int?, right: Int?): Double {
    if (left == 0 || left == null || right == null || right == 0) // log0 = NaN
        return 0.0
    val total: Double = (left + right).toDouble()
    val pLeft = -(left / total) * log2((left / total))
    val pRight = -(right / total) * log2((right / total))
    return pLeft + pRight
}

/**
 * Entropy
 * InfoX(D) = ∑[(|Dj| / |D|) * Info(Dj)]
 */
@JvmName("functionOfKotlin")
fun info(pairs: Collection<Int>): Double {
    val total: Double = pairs.sumOf { it }.toDouble()
    var result = 0.0

    for (it in pairs) {
        if (it == 0)
            return 0.0
        result += -(it / total) * log2(it / total)
    }
    return result
}

/**
 * Entropy
 * InfoX(D) = ∑[(|Dj| / |D|) * Info(Dj)]
 */
@JvmName("functionOfKotlin")
fun info(income: List<String>): Double {
    val freq = income.groupingBy { it }.eachCount()
    val probabilities = freq.values.map { it.toDouble() / income.size }
    return -probabilities.sumOf { p ->
        if (p == 0.0)
            p * 0.0
        p * log2(p)
    }

}


/**
 * Entropy
 * Info(D) = -∑[pI * log2(pI)]
 */
fun info(pair: MPair<Int?, Int?>): Double {
    if (pair.left == 0 || pair.left == null || pair.right == null || pair.right == 0) // log0 = NaN
        return 0.0
    val left = pair.left!!
    val right = pair.right!!
    val total: Double = (left + right).toDouble()
    val pLeft = -(left / total) * log2((left / total))
    val pRight = -(right / total) * log2((right / total))
    return pLeft + pRight
}


/**
 * Entropy
 * InfoX(D) = ∑[(|Dj| / |D|) * Info(Dj)]
 */
fun info(pairs: List<MPair<Int, Int>>): Double {
    val total: Double = pairs.sumOf { it.left + it.right }.toDouble()
    var result = 0.0

    for (it in pairs) {
        val info = info(it.left, it.right)
        result += ((it.left + it.right) / total) * info
    }
    return result
}

/**
 * Entropy
 * InfoX(D) = ∑[(|Dj| / |D|) * Info(Dj)]
 */
fun info(map: Map<String, Map<String, Int>>): Double {
    val all: Double = map.values.sumOf { it.values.sum() }.toDouble() // 14
    var result = 0.0
    for (inMap in map.values) {
        val total = inMap.values.sum() // 5
        val nextInfo = info(inMap.values)
        result += (total / all) * nextInfo
    }
    return result
}

/**
 * Entropy
 * InfoX(D) = ∑[(|Dj| / |D|) * Info(Dj)]
 */
@JvmName("functionOfKotlin")
fun info(map: Map<String, Map<String, List<Int>>>): Double {
    val all: Double = map.values.sumOf { it.values.sumOf { data -> data.size } }.toDouble() // 14
    var result = 0.0
    for (inMap in map.values) {
        val total = inMap.values.sumOf { it.size } // 5
        inMap.forEach {
            result += (total / all) * info(it.value)
        }
    }
    return result
}


/**
 * Gain(X) = Info(D) - InfoX(D)
 */
fun gain(info: Double, infoX: Double): Double {
    return info - infoX
}

fun findBestGain(dataset: MutableMap<String, MutableList<String>>, targetKey: String): String {
    val _target = dataset[targetKey]
    _target?.let { target ->
        val targetEntropy = info(target)

        val max = mutableMapOf<String, Double>()

        for (idx in dataset.keys) {
            if (idx == targetKey) continue
            dataset[idx]?.let { list ->
                val info = info(countInfo(list, target))
                val gain = gain(targetEntropy, info)
                max.getOrDefault(idx, -1.0).let {
                    if (gain >= it) {
                        max[idx] = gain
                    }
                }
            }
        }

        if (max.isEmpty())
            return "empty"
        return max.maxWith(compareBy { it.value }).key
    }
    return "?"
}

fun countInfo(data: List<String>): Map<String, Int> {
    val counts = mutableMapOf<String, Int>()
    for (next in data) {
        counts[next] = counts.getOrDefault(next, 0) + 1
    }
    return counts
}

fun countInfo(base: List<String>, target: List<String>): Map<String, Map<String, Int>> {
    val counts = mutableMapOf<String, MutableMap<String, Int>>()
    for (name in base) {
        if (!counts.containsKey(name)) {
            counts[name] = mutableMapOf()
            for (value in target) {
                counts[name]!![value] = 0
            }
        }
    }

    for (i in base.indices) {
        counts[base[i]]!![target[i]] = counts[base[i]]!![target[i]]!! + 1
    }
    return counts
}

fun countInfoByIndex(base: List<String>, target: List<String>): Map<String, Map<String, MutableList<Int>>> {
    val counts = mutableMapOf<String, MutableMap<String, MutableList<Int>>>()

    for (age in base) {
        if (!counts.containsKey(age)) {
            counts[age] = mutableMapOf()
            for (value in target) {
                counts[age]!![value] = mutableListOf()
            }
        }
    }

    for (i in base.indices) {
        counts[base[i]]!![target[i]]!!.add(i)
    }

    return counts
}

fun groupByValue(list: List<String>): Map<String, List<Int>> {
    return list.mapIndexed { index, value ->
        value to index
    }.groupBy({ it.first }, { it.second })
}

