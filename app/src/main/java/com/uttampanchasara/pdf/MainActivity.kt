package com.uttampanchasara.pdf

import android.os.Bundle
import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ListAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ListPopupWindow
import com.uttampanchasara.pdfgenerator.CreatePdf
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), CreatePdf.PdfCallbackListener {

    override fun onSuccess(filePath: String) {
        Toast.makeText(this, "Pdf Saved at: $filePath", Toast.LENGTH_SHORT).show()
    }

    override fun onFailure(errorMsg: String) {
        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show()
    }

    private var openPrintDialog: Boolean = false

    private var html =
        "<html xmlns=\\\"http://www.w3.org/1999/xhtml\\\">" +
                "<head>" +
                "<meta http-equiv=\\\"Content-Type\\\" " +
                "content=\\\"text/html; charset=utf-8\\\">" +
                "<title>Lorem Ipsum</title>" +
                "</head>" +
                "<body style=\\\"width:300px; color: #fda000; \\\">" +
                "<p color=\\\" \\\"><strong> About us</strong> </p>" +
                "<p><strong> Lorem Ipsum</strong> is simply dummy text .</p>" +
                "<p><strong> Lorem Ipsum</strong> is simply dummy text </p>" +
                "<p><strong> Lorem Ipsum</strong> is simply dummy text </p>" +
                "</body>" +
                "</html>"

    private val html2 = "<html><body>" +
            "<h2>This is a HTML Heading in Android WebView.</h2>\n" +
            "<p>This is a HTML paragraph in android WebView.</p>\n" +
            "\n" +
            "<h4>Following is HTML Table in WebView</h4>\n" +
            "<table border='1' style=\"{border-collapse: collapse;}\" width=\"100%\">\n" +
            "  <tr border=\"1\">\n" +
            "    <td><font color='#fda201'>Androidsdf sf s sf sf f fsfsfsfsf fsdfsdf dsfsfsd fsdf sd fsdf sd" +
            " fsdf d" +
            "sf " +
            "sdf sdf sdfsd fsd sd sdffd sf</font></td>\n" +
            "    <td>WebView</td>\t\t\n" +
            "    <td>100</td>\n" +
            "    <td>100</td>\n" +
            "    <td>100</td>\n" +
            "    <td>100 sdfdsf sf </td>\n" +
            "    <td>100sdf sfd dfsfsfsfd sf sd fsdf sdf sd</td>\n" +
            "    <td>100</td>\n" +
            "    <td>100sdf sf f f</td>\n" +
            "    <td>100sdf sf f f</td>\n" +
            "    <td>100sdf sf f f</td>\n" +
            "  </tr>\n" +
            "  <tr>\n" +
            "    <td>Android</td>\n" +
            "    <td>WebView</td>\t\t\n" +
            "    <td>200</td>\n" +
            "  </tr>\n" +
            "  <tr>\n" +
            "    <td>Android</td>\n" +
            "    <td>WebView</td>\t\t\n" +
            "    <td>300</td>\n" +
            "  </tr>\n" +
            "  <tr>\n" +
            "    <td>New item</td>\n" +
            "    <td>WebView</td>\t\t\n" +
            "    <td>400</td>\n" +
            "  </tr>\n" +
            "  <tr>\n" +
            "    <td>New item</td>\n" +
            "    <td>WebView</td>\t\t\n" +
            "    <td>400</td>\n" +
            "  </tr>\n" +
            "  <tr>\n" +
            "    <td>New item</td>\n" +
            "    <td>WebView</td>\t\t\n" +
            "    <td>400</td>\n" +
            "  </tr>\n" +
            "</table>" +
            "</body></html>";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView.loadDataWithBaseURL(null, html2, "text/html", "UTF-8", null)

        btnPrint.setOnClickListener {
            openPrintDialog = false
            doPrint()
            showListPopupWindow(it)
        }

        btnPrintAndSave.setOnClickListener {
            openPrintDialog = true
            doPrint()
        }
    }

    private fun doPrint() {
        CreatePdf(this)
            .setPdfName("Sample")
            .openPrintDialog(openPrintDialog)
            .setContentBaseUrl(null)
            .setContent(html2)
            .setCallbackListener(this)
            .create()
    }


    private fun showListPopupWindow(anchor: View) {
        val formatVideoPopupModels = ArrayList<FormatVideoPopupModel>()
        formatVideoPopupModels.add(FormatVideoPopupModel("همه", android.R.drawable.btn_minus))
        formatVideoPopupModels.add(FormatVideoPopupModel("انیمیشن", android.R.drawable.btn_minus))
        formatVideoPopupModels.add(FormatVideoPopupModel("فیلم و ویدئو", android.R.drawable.btn_minus))
        formatVideoPopupModels.add(FormatVideoPopupModel("اینفوگرافیک", android.R.drawable.btn_minus))
        formatVideoPopupModels.add(FormatVideoPopupModel("عکس", android.R.drawable.btn_minus))
        formatVideoPopupModels.add(FormatVideoPopupModel("پادکست (صوتی)", android.R.drawable.btn_minus))
        formatVideoPopupModels.add(FormatVideoPopupModel("متن و مقاله", android.R.drawable.btn_minus))

        val listPopupWindow = createListPopupWindow(anchor, ViewGroup.LayoutParams.WRAP_CONTENT, formatVideoPopupModels)
        listPopupWindow.setOnItemClickListener { parent, view, position, id ->
            listPopupWindow.dismiss()
            Toast.makeText(this, "clicked at $position", Toast.LENGTH_SHORT)
                .show()
        }
        listPopupWindow.show()
    }

    private fun createListPopupWindow(
        anchor: View, width: Int,
        items: List<FormatVideoPopupModel>
    ): ListPopupWindow {
        val popup = ListPopupWindow(this)
        val adapter = FormatVideoPopupAdapter(items)
        popup.anchorView = anchor
        popup.horizontalOffset = resources.getDimension(R.dimen.pop_up_window_margin_right).toInt()
        popup.width = measureContentWidth(adapter)
        popup.setAdapter(adapter)
        return popup
    }

    private fun measureContentWidth(listAdapter: ListAdapter): Int {
        var mMeasureParent: ViewGroup? = null
        var maxWidth = 0
        var itemView: View? = null
        var itemType = 0

        val widthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        val heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        val count = listAdapter.count
        for (i in 0 until count) {
            val positionType = listAdapter.getItemViewType(i)
            if (positionType != itemType) {
                itemType = positionType
                itemView = null
            }

            if (mMeasureParent == null) {
                mMeasureParent = FrameLayout(this)
            }

            itemView = listAdapter.getView(i, itemView, mMeasureParent)
            itemView!!.measure(widthMeasureSpec, heightMeasureSpec)

            val itemWidth = itemView.measuredWidth

            if (itemWidth > maxWidth) {
                maxWidth = itemWidth
            }
        }

        return Math.max(maxWidth, resources.getDimension(R.dimen.pop_up_window_min_width).toInt())
    }
}