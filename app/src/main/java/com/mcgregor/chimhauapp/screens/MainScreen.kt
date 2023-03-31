package com.mcgregor.chimhauapp.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Environment
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Link
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.TextAlignment
import com.itextpdf.layout.property.UnitValue
import com.mcgregor.chimhauapp.BuildConfig
import com.mcgregor.chimhauapp.R
import com.mcgregor.chimhauapp.models.ProductTransaction
import com.mcgregor.chimhauapp.viewmodels.ProductViewModel
import com.mcgregor.chimhauapp.widgets.AddFb
import com.mcgregor.chimhauapp.widgets.AppBar
import java.io.File

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(
    navController: NavController,
    selectedProductList: MutableList<ProductTransaction>,
    viewModel: ProductViewModel = hiltViewModel(),
) {

    val productsList = viewModel.products.collectAsState(initial = emptyList())
    val totalBillAmount = remember { mutableStateOf(0.0) }
    val context = LocalContext.current

    Scaffold(
        topBar = { AppBar(navController) },
        floatingActionButton = { AddFb(navController) }
    ) {
        BillList(selectedProductList, totalBillAmount, navController, context)
    }
}

@Composable
fun BillList(
    selectedProductList: MutableList<ProductTransaction>,
    totalBillAmount: MutableState<Double>,
    navController: NavController, context: Context
) {
    //get screen width
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val columnPortionSize = screenWidth / 4
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(4.dp)
    ) {


        if (selectedProductList.isEmpty()) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(start = 15.dp, end = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Your invoice bill is empty please click the icon on the top right corner to create" +
                            " a new product or click the + icon on the bottom right corner to start a new transaction",
                    color = Color.Gray
                )
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 10.dp), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Description", modifier = Modifier.width(columnPortionSize + 30.dp))
            Text(text = "Quantity", modifier = Modifier.width(columnPortionSize - 10.dp))
            Text(text = "Unit Price", modifier = Modifier.width(columnPortionSize - 10.dp))
            Text(text = "Amount", modifier = Modifier.width(columnPortionSize - 10.dp))
        }

        Divider(modifier = Modifier.padding(top = 6.dp, bottom = 6.dp))

        LazyColumn {
            items(selectedProductList) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp), horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = it.product.productName,
                        modifier = Modifier.width(columnPortionSize + 30.dp)
                    )
                    Text(
                        text = it.productQuantity.toString(),
                        modifier = Modifier.width(columnPortionSize - 10.dp)
                    )
                    Text(
                        text = "M${it.product.productPrice}",
                        modifier = Modifier.width(columnPortionSize - 10.dp)
                    )
                    Text(
                        text = "M${it.productTotalAmount}",
                        modifier = Modifier.width(columnPortionSize - 10.dp)
                    )
                }
            }
        }

        Divider(modifier = Modifier.padding(top = 6.dp, bottom = 6.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 20.dp), horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Total Amount  M${
                    calculateTotalAmount(
                        selectedProductList,
                        totalBillAmount
                    )
                }", fontWeight = FontWeight.Bold
            )
        }
        Button(onClick = {
            createPDF(selectedProductList, totalBillAmount, context)
        }) {
            Text(text = "Generate Pdf")
        }
    }
}

fun calculateTotalAmount(
    selectedProductList: MutableList<ProductTransaction>,
    totalBillAmount: MutableState<Double>
): Double {
    val totalAmountsList = mutableListOf<Double>()
    for (i in selectedProductList) {
        totalAmountsList.add(i.productTotalAmount)
    }
    return totalAmountsList.sumOf { it }
}

fun createPDF(
    selectedProductList: MutableList<ProductTransaction>,
    totalBillAmount: MutableState<Double>,
    context: Context
) {
    var grandTotal = 0.0
    for(productTransaction in selectedProductList) {
        grandTotal += productTransaction.productTotalAmount
    }
    val fileLocation = "example.pdf"
    val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileLocation)

    /*if (!file.exists()) {
        file.createNewFile()
    }*/
    val pdfDocument = PdfDocument(PdfWriter(file))
    pdfDocument.defaultPageSize = PageSize.A4
    val document = Document(pdfDocument)

    val table0 = Table(UnitValue.createPercentArray(floatArrayOf(50f,50f))).useAllAvailableWidth()
    table0.addHeaderCell(Cell().add(Paragraph("To: Kudzai Wilson\nHa Leqele\nMaseru 100")))
    table0.addHeaderCell(Cell().add(Paragraph("To: Kudzai Wilson\nHa Leqele\nMaseru 100")))
    document.add(table0)
    /*val companyAddress = Paragraph("To: Chimhau Scrap\n100 Maseru\nHa Leqele").setTextAlignment(TextAlignment.RIGHT)
    document.add(companyAddress)*/
    val boldText = Paragraph("Invoice Bill")
    boldText.setMarginTop(10f)
    boldText.setTextAlignment(TextAlignment.CENTER)
    document.add(boldText)
    val table = Table(UnitValue.createPercentArray(floatArrayOf(40f, 20f, 20f, 20f))).useAllAvailableWidth()


    //Add Header Cells
    table.addHeaderCell(Cell().add(Paragraph("Description").setTextAlignment(TextAlignment.CENTER)))
    table.addHeaderCell(Cell().add(Paragraph("Quantity").setTextAlignment(TextAlignment.CENTER)))
    table.addHeaderCell(Cell().add(Paragraph("Unit Price /kg").setTextAlignment(TextAlignment.CENTER)))
    table.addHeaderCell(Cell().add(Paragraph("Amount").setTextAlignment(TextAlignment.CENTER)))

    for (productTransaction in selectedProductList) {
        table.addCell(Cell().add(Paragraph(productTransaction.product.productName).setTextAlignment(TextAlignment.CENTER)))
        table.addCell(Cell().add(Paragraph(productTransaction.productQuantity.toString()))).setTextAlignment(TextAlignment.CENTER)
        table.addCell(Cell().add(Paragraph(productTransaction.product.productPrice).setTextAlignment(TextAlignment.CENTER)))
        table.addCell(Cell().add(Paragraph("M"+productTransaction.productTotalAmount.toString()).setTextAlignment(TextAlignment.CENTER)))
    }

    table.addCell(Cell().add(Paragraph().setTextAlignment(TextAlignment.CENTER)))
    table.addCell(Cell().add(Paragraph().setTextAlignment(TextAlignment.CENTER)))
    table.addCell(Cell().add(Paragraph("Total").setTextAlignment(TextAlignment.CENTER)))
    table.addCell(Cell().add(Paragraph("M${grandTotal}").setTextAlignment(TextAlignment.CENTER)))


    document.add(table)
    document.close()

    val uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file)
    val intent = Intent(Intent.ACTION_VIEW)
    intent.setDataAndType(uri, "application/pdf")
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    context.startActivity(intent)
}


