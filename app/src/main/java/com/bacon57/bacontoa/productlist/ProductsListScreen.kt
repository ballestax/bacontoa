package com.bacon57.bacontoa.productlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.bacon57.bacontoa.R
import com.bacon57.bacontoa.model.Category
import com.bacon57.bacontoa.model.CategoryList
import com.bacon57.bacontoa.model.Product
import com.bacon57.bacontoa.model.ProductOrder
import com.bacon57.bacontoa.ui.theme.*
import com.bacon57.bacontoa.util.Constants
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*
import kotlin.streams.toList


@Composable
fun ProductsListScreen(
    navController: NavController, viewModel: ProductListViewModel = hiltViewModel()
) {
    var selectedTabIndex by remember {
        mutableStateOf(0)
    }
    Column {
        Spacer(modifier = Modifier.height(16.dp))
        SearchBar(
            hint = "Search...", modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            //viewModel.searchPokemonList(it)
        }
        Spacer(modifier = Modifier.height(16.dp))
        ProductCategoriesSection(
            modifier = Modifier.fillMaxWidth(),
            categoryList = listOf(
                Category(1, "hamburguesas"),
                Category(2, "carnes"),
                Category(3, "perros calientes"),
                Category(4, "bebidas"),
                Category(5, "menu infntil"),
                Category(4, "micheladas"),
            )
        ) {
            selectedTabIndex = it
        }
        when (selectedTabIndex) {
            0 -> {
                ProductList(navController = navController, filter = "hamburguesas")
            }
            1 -> {
                ProductList(navController = navController, filter = "carnes")
            }
            2 -> {
                ProductList(navController = navController, filter = "perros calientes")
            }
            3 -> {
                ProductList(navController = navController, filter = "bedidas")
            }
            4 -> {
                ProductList(navController = navController, filter = "menu infantil")
            }
            5 -> {
                ProductList(navController = navController, filter = "micheladas")
            }
        }
    }
}

@Composable
fun ProductList(
    navController: NavController, viewModel: ProductListViewModel = hiltViewModel(), filter: String
) {
    val productList by remember { viewModel.productList }
    val loadError by remember { viewModel.loadError }
    val isLoading by remember { viewModel.isLoading }
    val isSearching by remember { viewModel.isSearching }

    var _productList = productList
    if(filter.isNotEmpty())
        _productList = productList.filter { product: Product -> product.category==filter }.toList()


    LazyVerticalGrid(
        columns = GridCells.Adaptive(240.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(_productList) { product ->
            ProductCard(product = product, navController = navController)
        }
    }
    Box(
        contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colors.primary)
        }
        if (loadError.isNotEmpty()) {
            RetrySection(error = loadError) {
                viewModel.viewModelScope.launch() { viewModel.loadProducts() }
            }
        }
    }
}

@Composable
fun ProductCategoriesSection(
    modifier: Modifier = Modifier,
    categoryList: List<Category>,
    onTabSelected: (selectedIndex: Int) -> Unit
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    println("::" + categoryList + Date())
    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        backgroundColor = Color.Transparent,
        contentColor = Color.Black,
        modifier = modifier
    ) {
        categoryList.forEachIndexed { index, category ->
            println(category.name + "::" + Date())
            Tab(
                selected = selectedTabIndex == index,
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.LightGray,
                onClick = {
                selectedTabIndex = index
                onTabSelected(index)
            }) {
                Box(
                    modifier = Modifier
                        .background(if (selectedTabIndex == index) SaddleBrown else BlackBean, CircleShape)
                        .padding(8.dp)
                        .shadow(2.dp, CircleShape)
                ) {
                    Row() {
//                        Icon(
//                            painter = painterResource(id = R.drawable.ic_baseline_lunch_dining_24),
//                            contentDescription = null,
//                            tint = if (selectedTabIndex == index) Color.Red else BlackBean
//                        )
                        Text(
                            text = category.name.capitalize(Locale.ROOT),
                            color = if (selectedTabIndex == index) WindsorTan else BlackBean,
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun ProductCard(
    product: Product,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: ProductListViewModel = hiltViewModel()
) {
    val format: NumberFormat = NumberFormat.getCurrencyInstance()
    format.maximumFractionDigits = 0
    Card(
        backgroundColor = BackgroundCard
    ) {
        Row(
            modifier = Modifier.padding(all = 8.dp),

            ) {
            val url = if (product.image == null) {
                Constants.DEFAULT_IMAGE_URL
            } else {
                product.image
            }
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(url).crossfade(true)
                    .build(),
                contentDescription = product.name,
                contentScale = ContentScale.Fit,
                modifier = modifier.size(70.dp),
                loading = {
                    CircularProgressIndicator(
                        color = MaterialTheme.colors.primary, modifier = Modifier.scale(0.5f)
                    )
                },
            )
            Column(modifier = Modifier.padding(start = 4.dp)) {
                Text(
                    text = product.name.toUpperCase(Locale.ROOT),
                    fontSize = 16.sp,
                    color = MaterialTheme.colors.secondaryVariant,
                    style = MaterialTheme.typography.subtitle2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row() {
                    Column() {
                        Text(
                            text = product.category.capitalize(Locale.ROOT),
                            fontSize = 12.sp,
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colors.primaryVariant,
                            style = MaterialTheme.typography.body1
                        )
                        Text(
                            text = format.format(product.price),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = WindsorTan,
                            style = MaterialTheme.typography.subtitle1
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        Button(modifier = Modifier
                            .height(40.dp)
                            .width(40.dp), onClick = {
                            navController.navigate(
                                "product_detail_screen/${product.id}"
                            )
                        }) {
                            Icon(
                                painterResource(id = R.drawable.ic_baseline_playlist_add_24),
                                contentDescription = null,
                                modifier = Modifier.height(50.dp)
                            )
                        }
                        Button(modifier = Modifier
                            .height(40.dp)
                            .width(40.dp), onClick = {
                                val productOrder = ProductOrder(
                                    productId = product.id,
                                    productName = product.name,
                                    presentationId = 0,
                                    quantity = 1,
                                    additions = emptyList(),
                                    exclusions = emptyList(),
                                    price = product.price.toBigDecimal()

                                )
                            navController.navigate(
                                "order_detail_screen/${productOrder}"
                            )

                        }) {
                            Image(
                                painterResource(id = R.drawable.ic_baseline_add_24),
                                contentDescription = null,
                                modifier = Modifier.height(25.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier, hint: String = "", onSearch: (String) -> Unit = {}
) {
    var text by remember {
        mutableStateOf("")
    }
    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }

    Box(modifier = modifier) {
        BasicTextField(value = text,
            onValueChange = {
                text = it
                onSearch(it)
            },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .onFocusChanged {
                    isHintDisplayed = it != FocusState::isFocused && text.isNotEmpty()
                })
        if (isHintDisplayed) {
            Text(
                text = hint,
                color = Color.LightGray,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
            )
        }
    }
}

@Composable
fun RetrySection(
    error: String, onRetry: () -> Unit
) {
    Column {
        Text(error, color = Color.Red, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { onRetry }, modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Retry")
        }
    }
}



