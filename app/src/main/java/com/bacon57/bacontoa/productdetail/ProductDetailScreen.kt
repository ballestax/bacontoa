package com.bacon57.bacontoa.productdetail

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.bacon57.bacontoa.R
import com.bacon57.bacontoa.model.Additional
import com.bacon57.bacontoa.model.Ingredient
import com.bacon57.bacontoa.model.Presentation
import com.bacon57.bacontoa.model.Product
import com.bacon57.bacontoa.ui.theme.*
import com.bacon57.bacontoa.util.Constants
import com.bacon57.bacontoa.util.Resource
import okhttp3.internal.format
import java.text.NumberFormat
import java.util.*

@Composable
fun ProductDetailScreen(
    productId: Long,
    navController: NavController,
    topPadding: Dp = 20.dp,
    productImageSize: Dp = 20.dp,
    viewModel: ProductDetailViewModel = hiltViewModel()
) {

    val productInfo = produceState<Resource<Product>>(initialValue = Resource.Loading()) {
        value = viewModel.getProductInfo(productId)
    }.value

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopBar(
            name = "Detalles del producto #${productId}",
            navController = navController,
            modifier = Modifier.padding(16.dp)
        )
        ProductDetailStateWrapper(
            productInfo = productInfo,
            navController = navController,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 16.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
                .shadow(5.dp, RoundedCornerShape(5.dp))
                .clip(RoundedCornerShape(10.dp))
                .background(BlackBean2),
            //.align(Alignment.BottomCenter),
            loadingModifier = Modifier
                .size(100.dp)
                // .align(Alignment.CenterHorizontally)
                .padding(
                    top = 16.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
        )
    }
}

@Composable
fun TopBar(
    name: String,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            tint = Color.Black,
            modifier = Modifier
                .size(36.dp)
                .clickable {
                    navController.popBackStack()
                }
        )
        Text(
            text = name,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
    }
}

@Composable
fun ProductDetailStateWrapper(
    productInfo: Resource<Product>,
    modifier: Modifier = Modifier,
    navController: NavController,
    loadingModifier: Modifier = Modifier
) {
    when (productInfo) {
        is Resource.Success -> {
            ProductDetailSection(
                productInfo = productInfo.data!!,
                modifier = modifier,
                navController = navController
            )
        }
        is Resource.Error -> {
            Text(
                text = productInfo.message!!,
                color = Color.Red,
                modifier = modifier
            )
        }
        is Resource.Loading -> {
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary,
                modifier = loadingModifier
            )
        }
    }
}

@Composable
fun ProductDetailSection(
    productInfo: Product,
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ProductDetailViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 10.dp,
                    vertical = 10.dp
                )
        ) {
            RoundImage(
                image = productInfo.image ?: Constants.DEFAULT_IMAGE_URL,
                name = productInfo.name,
                modifier = Modifier
                    .size(100.dp)
                    .weight(3f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            InfoSection(modifier = Modifier.weight(7f), product = productInfo)
        }

        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxSize()
        ) {
            if (productInfo.presentations.isNotEmpty()) {
                ProductPresentationSection(presentations = productInfo.presentations)
            }
            if (productInfo.ingredients.isNotEmpty()) {
                ProductIngredientSection(ingredients = productInfo.ingredients)
            }
            if (productInfo.additionals.isNotEmpty()) {
                ProductAdditionalSection(additions = productInfo.additionals)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                modifier = Modifier,
                onClick = {

                }
            ) {
                Text(text = "Cancelar")
            }
            Button(
                modifier = Modifier,
                onClick = {
                    val productOrder =
                        viewModel.getProductOrder(productInfo, productInfo.price.toBigDecimal())
                    navController.navigate(
                        "order_detail_screen/${productOrder}"
                    )
                }
            ) {
                Text(text = "Aceptar")
            }
        }

//        PokemonDetailDataSection(
//            pokemonWeight = pokemonInfo.weight,
//            pokemonHeight = pokemonInfo.height
//        )
//        PokemonBaseStats(pokemonInfo = pokemonInfo)
    }

}

@Composable
fun RoundImage(
    image: String,
    name: String,
    modifier: Modifier = Modifier
) {
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(image)
            .crossfade(true)
            .build(),
        contentDescription = name,
        error = { painterResource(id = R.drawable.ic_launcher_background) },
        modifier = modifier
            .aspectRatio(1f, matchHeightConstraintsFirst = true)
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = CircleShape
            )
            .padding(3.dp)
            .clip(CircleShape)
    )
}

@Composable
fun InfoSection(product: Product, modifier: Modifier = Modifier) {
    val format: NumberFormat = NumberFormat.getCurrencyInstance()
    format.maximumFractionDigits = 0
    Column(modifier = modifier) {
        Text(
            text = product.name.uppercase(),
            fontWeight = FontWeight.Bold,
            color = WindsorTan,
            fontSize = 20.sp
        )
        Text(
            text = product.category.capitalize(Locale.ROOT),
            fontStyle = FontStyle.Italic,
            fontSize = 14.sp,
            color = SatinSheenGold
        )
        Text(
            text = format.format(product.price),
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            color = SatinSheenGold
        )
    }
}


@Composable
fun ProductPresentationSection(
    presentations: List<Presentation>,
    viewModel: ProductDetailViewModel = hiltViewModel()
) {
    var selectedPresentation by remember { mutableStateOf(presentations.first { it._default }) }
    Box(
        Modifier
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        BlackBean,
                        BlackBean2
                    )
                ),
                shape = RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 8.dp)

    ) {
        Text(
            text = "Presentaciones",
            modifier = Modifier
                .fillMaxWidth()
        )
    }
    LazyVerticalGrid(
        modifier = Modifier.heightIn(min = 100.dp, max = 200.dp),
        columns = GridCells.Adaptive(160.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),

        ) {
        items(presentations) { presentation ->
            PresentationProduct(
                presentation = presentation,
                selected = presentation == selectedPresentation,
                onClick = {
                    selectedPresentation = presentation
                    viewModel.selectPresentation(presentation.id)
                }
            )
        }
    }
}

@Composable
fun ProductIngredientSection(
    ingredients: List<Ingredient>,
    viewModel: ProductDetailViewModel = hiltViewModel()
) {
    Box(
        Modifier
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        BlackBean,
                        BlackBean2
                    )
                ),
                shape = RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 8.dp)

    ) {
        Text(
            text = "Ingredientes",
            modifier = Modifier
                .fillMaxWidth()
        )
    }
    LazyVerticalGrid(
        modifier = Modifier.heightIn(min = 150.dp, max = 200.dp),
        columns = GridCells.Adaptive(160.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(ingredients) { ingredient ->
            IngredientProduct(
                ingredient = ingredient,
                selected = viewModel.selectedIngredients.contains(ingredient),
                onClick = { viewModel.toggleIngredientSelection(ingredient) }
            )
        }
    }
}

@Composable
fun ProductAdditionalSection(
    additions: List<Additional>,
    viewModel: ProductDetailViewModel = hiltViewModel()
) {
    Box(
        Modifier
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        BlackBean,
                        BlackBean2
                    )
                ),
                shape = RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 8.dp)

    ) {
        Text(
            text = "Adicionales",
            modifier = Modifier
                .fillMaxWidth()
        )
    }
    LazyVerticalGrid(
        modifier = Modifier.heightIn(min = 100.dp, max = 300.dp),
        columns = GridCells.Adaptive(200.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),

        ) {
        items(additions) { addition ->
            AdditionalProduct(
                additional = addition,
                selected = viewModel.selectedAdditional.contains(addition),
                quantity = 0,
                onClick = { viewModel.toggleAdditionalSelection(addition) }
            )
        }
    }
}

@Composable
fun PresentationProduct(
    presentation: Presentation,
    modifier: Modifier = Modifier,
    selected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (selected) Manatee else RaisinBlack
    val borderColor = if (selected) SatinSheenGold else RaisinBlack
    Row(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .background(color = backgroundColor, shape = RoundedCornerShape(8.dp))
            .border(BorderStroke(2.dp, borderColor), RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {

        RadioButton(
            selected = selected,
            onClick = onClick,
            enabled = true,
            colors = RadioButtonDefaults.colors(
                selectedColor = SatinSheenGold
            )
        )
        Column(modifier = Modifier.padding(horizontal = 8.dp)) {
            Text(
                text = presentation.name.uppercase(),
                style = MaterialTheme.typography.body1.merge(),
                fontSize = 14.sp
            )
            Text(
                text = String.format("$%,.0f", presentation.price),
                style = MaterialTheme.typography.body1.merge(),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun IngredientProduct(
    ingredient: Ingredient,
    selected: Boolean,
    onClick: () -> Unit
) {
    val isChecked = remember { mutableStateOf(selected) }
    val backgroundColor = if (isChecked.value) Manatee else RaisinBlack
    Row(
        modifier = Modifier
            .padding(8.dp)
            .background(color = backgroundColor, shape = RoundedCornerShape(5.dp))
            .clickable(onClick = {
                onClick()
                isChecked.value = !isChecked.value
            }),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked.value,
            onCheckedChange = {
                isChecked.value = it
                onClick()
            }
        )
        Text(text = ingredient.name.capitalize(Locale.ROOT))
    }
}

@Composable
fun AdditionalProduct(
    additional: Additional,
    selected: Boolean,
    quantity: Int,
    onClick: () -> Unit
) {
    val isChecked = remember { mutableStateOf(selected) }
    val backgroundColor = if (isChecked.value) Manatee else RaisinBlack
    Row(
        modifier = Modifier
            .padding(8.dp)
            .background(color = backgroundColor, shape = RoundedCornerShape(5.dp))
            .clickable(onClick = {
                onClick
                isChecked.value = !isChecked.value
            }),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked.value,
            onCheckedChange = {
                onClick()
                isChecked.value = it
            }
        )
        Column() {
            Text(text = additional.name.capitalize(Locale.ROOT))
            Text(
                text = String.format("$%,.0f", additional.price),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}






