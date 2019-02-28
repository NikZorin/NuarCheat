package com.example.nzorin.kotlinandroidtest.utils

import com.example.nzorin.kotlinandroidtest.entity.Card

object MergeUtils {

    fun mergeLists(first: List<Card>, second: List<Card>): List<Card> {
        val res = arrayListOf<Card>()
        if (first.isEmpty()) {
            return second
        }

        if (second.isEmpty()) {
            return first
        }

        for(f in first) {
            for(s in second) {
                if (f.name == s.name) {
                    res.add(f)
                }
            }
        }
        return res
    }

    fun cutLists(first: List<Card>, second: List<Card>): List<Card> {
        val res = arrayListOf<Card>()
        if (first.isEmpty()) {
            return arrayListOf()
        }

        if (second.isEmpty()) {
            return first
        }

        for(f in first) {
            var found = false
            for(s in second) {
                if (f.name == s.name) {
                    found = true
                }
            }
            if (!found) {
                res.add(f)
            }
        }
        return res
    }

    fun unionLists(first: List<Card>, second: List<Card>): List<Card> {
        val res = arrayListOf<Card>()
        if (first.isEmpty()) {
            return second
        }

        if (second.isEmpty()) {
            return first
        }

        res.addAll(first)

        for(s in second) {
            var found = false
            for(r in res) {
                if (s.name == r.name) {
                    found = true
                }
            }
            if (!found) {
                res.add(s)
            }
        }

        return res
    }
}
