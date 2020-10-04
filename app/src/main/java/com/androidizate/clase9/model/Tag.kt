package com.androidizate.clase9.model

/**
 * @author Andres Oller
 */
class Tag {
    var id = 0L
    var tagName: String = ""

    constructor() {}
    constructor(tag_name: String) {
        tagName = tag_name
    }

    constructor(id: Long, tag_name: String) {
        this.id = id
        tagName = tag_name
    }

}