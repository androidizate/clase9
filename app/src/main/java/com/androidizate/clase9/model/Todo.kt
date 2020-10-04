package com.androidizate.clase9.model

/**
 * @author Andres Oller
 */
class Todo {
    var id = 0L
    var note: String = ""
    var status = 0
    var created_at: String = ""

    // constructors
    constructor() {}
    constructor(note: String, status: Int) {
        this.note = note
        this.status = status
    }

    constructor(id: Long, note: String, status: Int) {
        this.id = id
        this.note = note
        this.status = status
    }
}