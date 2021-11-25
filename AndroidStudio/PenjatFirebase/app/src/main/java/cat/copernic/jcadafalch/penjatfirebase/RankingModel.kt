package cat.copernic.jcadafalch.penjatfirebase

import java.sql.Timestamp


data class RankingModel(var username: String ?= null, var points: Int ?= null, var date: Timestamp ?= null) {

}