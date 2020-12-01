package com.project.recipedemo.usecase

import android.content.Context
import com.project.recipedemo.model.RecipeType
import io.reactivex.Single
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.InputStream
import javax.inject.Inject
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

class GetRecipeTypesExecutor @Inject constructor(
    private val context: Context
) {
    companion object {
        private const val RECIPE_TYPE = "recipetype"
        private const val DIRECTORY_FILE = "recipetypes.xml"
        private const val RECIPE_TYPE_ID = "id"
        private const val RECIPE_TYPE_TITLE = "title"
    }

    fun execute(isAllItem: Boolean = true): Single<List<RecipeType>> {
        val listRecipeType = mutableListOf<RecipeType>()
        try {
            val inputStream: InputStream = context.assets.open(DIRECTORY_FILE)
            val dbFactory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
            val dBuilder: DocumentBuilder = dbFactory.newDocumentBuilder()
            val doc: Document = dBuilder.parse(inputStream)
            val element: Element = doc.documentElement
            element.normalize()
            val nList: NodeList = doc.getElementsByTagName(RECIPE_TYPE)
            if (!isAllItem) {
                for (i in 1 until nList.length) {
                    val node = nList.item(i)
                    if (node.nodeType == Node.ELEMENT_NODE) {
                        val element2 = node as Element
                        val id = getValue(RECIPE_TYPE_ID, element2)
                        val title = getValue(RECIPE_TYPE_TITLE, element2)
                        listRecipeType.add(RecipeType(id!!.toInt(), title!!))
                    }
                }
            } else {
                for (i in 0 until nList.length) {
                    val node = nList.item(i)
                    if (node.nodeType == Node.ELEMENT_NODE) {
                        val element2 = node as Element
                        val id = getValue(RECIPE_TYPE_ID, element2)
                        val title = getValue(RECIPE_TYPE_TITLE, element2)
                        listRecipeType.add(RecipeType(id!!.toInt(), title!!))
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Single.just(listRecipeType)
    }

    private fun getValue(tag: String, element: Element): String? {
        val nodeList: NodeList = element.getElementsByTagName(tag).item(0).childNodes
        val node: Node = nodeList.item(0)
        return node.nodeValue
    }
}
