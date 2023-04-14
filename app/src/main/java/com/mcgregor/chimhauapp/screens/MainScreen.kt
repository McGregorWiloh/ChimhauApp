package com.mcgregor.chimhauapp.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.viewbinding.BuildConfig
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.LineSeparator
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.TextAlignment
import com.itextpdf.layout.property.UnitValue
import com.mcgregor.chimhauapp.R
import com.mcgregor.chimhauapp.models.ProductTransaction
import com.mcgregor.chimhauapp.viewmodels.ProductViewModel
import com.mcgregor.chimhauapp.widgets.AddFb
import com.mcgregor.chimhauapp.widgets.AppBar
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

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
    val sellerNameAndSurname = remember { mutableStateOf("") }
    val sellerAddress = remember { mutableStateOf("") }
    val openDialog = remember { mutableStateOf(false) }

    Scaffold(
        topBar = { AppBar(navController) },
        floatingActionButton = { AddFb(navController) }
    ) {
        BillList(
            selectedProductList,
            totalBillAmount,
            sellerNameAndSurname,
            sellerAddress,
            openDialog,
            navController,
            context
        )
    }
}

@Composable
fun BillList(
    selectedProductList: MutableList<ProductTransaction>,
    totalBillAmount: MutableState<Double>,
    sellerNameAndSurname: MutableState<String>,
    sellerAddress: MutableState<String>,
    openDialog: MutableState<Boolean>,
    navController: NavController, context: Context
) {
    //get screen width
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val columnPortionSize = screenWidth / 4
    val context = LocalContext.current
    val isDarkMode = isSystemInDarkTheme()

    val textColor = when(isDarkMode) {
        true -> Color.White
        false -> Color.Black
    }

    val backgroundColor = when(isDarkMode) {
        true -> Color.Black
        false -> Color.White
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundColor)
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
                        text = "M${String.format("%.2f", it.product.productPrice.toDoubleOrNull())}",
                        modifier = Modifier.width(columnPortionSize - 10.dp)
                    )
                    Text(
                        text = "M${String.format("%.2f", it.productTotalAmount)}",
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
                text = "Total Amount  M${ String.format("%.2f", calculateTotalAmount(
                    selectedProductList,
                    totalBillAmount
                ))
                }", fontWeight = FontWeight.Bold
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = {
                createPDF(selectedProductList, sellerNameAndSurname, sellerAddress, context)
            }) {
                Text(text = "Generate Pdf")
            }

            Button(onClick = {
                openDialog.value = true

            }) {
                Text(text = "Enter seller info")
            }
        }

        if (openDialog.value) {

            AlertDialog(
                onDismissRequest = {
                    // Dismiss the dialog when the user clicks outside the dialog or on the back
                    // button. If you want to disable that functionality, simply use an empty
                    // onCloseRequest.
                    openDialog.value = false
                },
                title = {
                    Text(text = "Seller Info")
                },
                text = {
                    Column() {
                        TextField(
                            value = sellerNameAndSurname.value,
                            label = { Text(text = "Name and surname") },
                            onValueChange = {
                                sellerNameAndSurname.value = it
                            })
                        TextField(
                            value = sellerAddress.value,
                            modifier = Modifier.padding(top = 10.dp),
                            label = { Text(text = "Seller Address") },
                            onValueChange = {
                                sellerAddress.value = it
                            })
                    }

                },
                confirmButton = {
                    Button(
                        onClick = {
                            openDialog.value = false
                            Toast.makeText(
                                context,
                                "Seller info saved successfully!!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    ) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            openDialog.value = false
                        }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                    ) {
                        Text("No")
                    }
                }
            )
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
    sellerNameAndSurname: MutableState<String>,
    sellerAddress: MutableState<String>,
    context: Context
) {
    var grandTotal = 0.0
    for (productTransaction in selectedProductList) {
        grandTotal += productTransaction.productTotalAmount
    }
    val fileLocation = "example.pdf"
    val file = File(context.filesDir, fileLocation)

    if (!file.exists()) {
        file.createNewFile()
    }
    val pdfDocument = PdfDocument(PdfWriter(file))
    pdfDocument.defaultPageSize = PageSize.A4
    val document = Document(pdfDocument)

    val line = SolidLine(0.1f)
    line.color = ColorConstants.BLACK
    val lineSeparator = LineSeparator(line)
    lineSeparator.setMarginBottom(20f)
    line.lineWidth = 1f

    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.chimhau_logo)
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    val bitmapData = stream.toByteArray()
    val imageData = ImageDataFactory.create(bitmapData)
    val image = Image(imageData)
    image.scaleAbsolute(70f,70f)
    document.add(image)
    document.add(lineSeparator)

    val table0 = Table(UnitValue.createPercentArray(floatArrayOf(50f, 50f))).useAllAvailableWidth()
    table0.addHeaderCell(
        Cell().add(
            Paragraph("From: ${sellerNameAndSurname.value}\n${sellerAddress.value}").setTextAlignment(
                TextAlignment.CENTER
            )
        )
    )
    table0.addHeaderCell(
        Cell().add(
            Paragraph("To: Chimhau Scrap\nHa Tikoe\nMaseru").setTextAlignment(
                TextAlignment.CENTER
            )
        )
    )
    document.add(table0)
    /*val companyAddress = Paragraph("To: Chimhau Scrap\n100 Maseru\nHa Leqele").setTextAlignment(TextAlignment.RIGHT)
    document.add(companyAddress)*/
    val boldText = Paragraph("Invoice Bill")

    val emptyParagraph = Paragraph("")
    //get current date and format it
    val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val currentDate = Calendar.getInstance().time
    val formattedDate = dateFormat.format(currentDate)
    val date = Paragraph("Invoice Date: " + formattedDate)

    boldText.setMarginTop(10f)
    boldText.setTextAlignment(TextAlignment.CENTER)
    document.add(boldText)

    date.setMarginTop(10f)
    date.setMarginBottom(10f)
    document.add(date)

    val table =
        Table(UnitValue.createPercentArray(floatArrayOf(40f, 20f, 20f, 20f))).useAllAvailableWidth()


    //Add Header Cells
    table.addHeaderCell(Cell().add(Paragraph("Description").setTextAlignment(TextAlignment.CENTER)))
    table.addHeaderCell(Cell().add(Paragraph("Quantity").setTextAlignment(TextAlignment.CENTER)))
    table.addHeaderCell(Cell().add(Paragraph("Unit Price /kg").setTextAlignment(TextAlignment.CENTER)))
    table.addHeaderCell(Cell().add(Paragraph("Amount").setTextAlignment(TextAlignment.CENTER)))

    for (productTransaction in selectedProductList) {
        table.addCell(
            Cell().add(
                Paragraph(productTransaction.product.productName).setTextAlignment(
                    TextAlignment.CENTER
                )
            )
        )
        table.addCell(Cell().add(Paragraph(productTransaction.productQuantity.toString())))
            .setTextAlignment(TextAlignment.CENTER)
        table.addCell(
            Cell().add(
                Paragraph(String.format("%.2f", productTransaction.product.productPrice.toDoubleOrNull())).setTextAlignment(
                    TextAlignment.CENTER
                )
            )
        )
        table.addCell(
            Cell().add(
                Paragraph("M" + String.format("%.2f", productTransaction.productTotalAmount)).setTextAlignment(
                    TextAlignment.CENTER
                )
            )
        )
    }

    table.addCell(Cell().add(Paragraph().setTextAlignment(TextAlignment.CENTER)))
    table.addCell(Cell().add(Paragraph().setTextAlignment(TextAlignment.CENTER)))
    table.addCell(Cell().add(Paragraph("Total").setTextAlignment(TextAlignment.CENTER)))
    table.addCell(Cell().add(Paragraph("M${String.format("%.2f", grandTotal)}").setTextAlignment(TextAlignment.CENTER)))
    document.add(table)

    val disclaimer = Paragraph(
        "I hereby state that I am the lawful owner of the materials listed above" +
                " and have sold them to Chimhau Scrap Company to dispose of as they see fit"
    )
    disclaimer.setMarginTop(50f)
    val nameAndSurname = Paragraph(sellerNameAndSurname.value)
    val signature = Paragraph("Signature")
    signature.setMarginBottom(50f)

    val contactDetails = Paragraph("Call or WhatsApp:  +266 5832 5213,  +266 6321 9775,  +266 2231 6585    Email: chimhaucashton@gmail.com")
    contactDetails.setFontSize(10f)
    contactDetails.setTextAlignment(TextAlignment.CENTER)
    lineSeparator.setMarginBottom(0.5f)

    document.add(emptyParagraph)
    document.add(emptyParagraph)
    document.add(disclaimer)
    document.add(nameAndSurname)
    document.add(emptyParagraph)
    document.add(signature)
    document.add(lineSeparator)
    document.add(contactDetails)
    document.close()

    val uri = FileProvider.getUriForFile(context, "com.mcgregor.chimhauapp.provider", file)
    val intent = Intent(Intent.ACTION_VIEW)
    intent.setDataAndType(uri, "application/pdf")
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    context.startActivity(intent)
}

fun drawableToBitmap(context: Context, drawable: Drawable): Bitmap {
    if(drawable is BitmapDrawable) {
        return drawable.bitmap
    }
    val bitmap = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable.setBounds(0,0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}


