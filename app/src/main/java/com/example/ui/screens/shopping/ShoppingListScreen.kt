package com.example.ui.screens.shopping

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.RemoveShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.entity.ShoppingItemEntity
import com.example.ui.theme.BorderLight
import com.example.ui.theme.CardSurface
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.SurfaceMuted
import com.example.ui.theme.TerracottaRed
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import com.example.ui.viewmodel.ShoppingViewModel

@Composable
fun ShoppingListScreen(
    viewModel: ShoppingViewModel,
    modifier: Modifier = Modifier
) {
    val shoppingState by viewModel.shoppingState.collectAsStateWithLifecycle()
    val inputText by viewModel.inputText.collectAsStateWithLifecycle()
    val showClearDialog by viewModel.showClearDialog.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CreamBackground)
            .statusBarsPadding()
    ) {
        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Lista de Compras",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    color = TextPrimary
                )
            )

            if (shoppingState.totalCount > 0) {
                Text(
                    text = "Borrar todo",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = TerracottaRed
                    ),
                    modifier = Modifier
                        .clickable { viewModel.setShowClearDialog(true) }
                        .padding(4.dp)
                        .testTag("clear_all_shopping_button")
                )
            }
        }

        // Input Bar: TextField + '+' button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { viewModel.onInputTextChange(it) },
                placeholder = {
                    Text(
                        text = "Agregar ingrediente...",
                        color = TextSecondary.copy(alpha = 0.8f),
                        fontSize = 15.sp
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = SurfaceMuted,
                    unfocusedContainerColor = SurfaceMuted,
                    focusedBorderColor = TerracottaRed.copy(alpha = 0.5f),
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { viewModel.addItem() }),
                modifier = Modifier
                    .weight(1f)
                    .testTag("add_ingredient_input")
            )

            Spacer(modifier = Modifier.width(10.dp))

            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(TerracottaRed)
                    .clickable { viewModel.addItem() }
                    .testTag("add_ingredient_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar ingrediente",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (shoppingState.totalCount == 0) {
            // Empty State
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(SurfaceMuted),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.RemoveShoppingCart,
                        contentDescription = null,
                        tint = TerracottaRed,
                        modifier = Modifier.size(46.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Tu lista de compras está vacía",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Agrega ingredientes manualmente arriba o desde la página de detalle de cualquier receta.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("shopping_items_list"),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Section: Pendientes
                if (shoppingState.pendingItems.isNotEmpty()) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Pendientes",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = TextPrimary
                                )
                            )
                            Text(
                                text = "${shoppingState.pendingItems.size}",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    color = TextSecondary
                                )
                            )
                        }
                    }

                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = CardSurface),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, BorderLight, RoundedCornerShape(20.dp))
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                shoppingState.pendingItems.forEachIndexed { index, item ->
                                    ShoppingItemRow(
                                        item = item,
                                        onToggle = { viewModel.toggleItem(item) },
                                        onDelete = { viewModel.deleteItem(item.id) }
                                    )
                                    if (index < shoppingState.pendingItems.size - 1) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(1.dp)
                                                .background(BorderLight.copy(alpha = 0.6f))
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Section: Completados
                if (shoppingState.completedItems.isNotEmpty()) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Completados",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = TextPrimary
                                )
                            )
                            Text(
                                text = "${shoppingState.completedItems.size}",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    color = TextSecondary
                                )
                            )
                        }
                    }

                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = CardSurface),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, BorderLight, RoundedCornerShape(20.dp))
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                shoppingState.completedItems.forEachIndexed { index, item ->
                                    ShoppingItemRow(
                                        item = item,
                                        onToggle = { viewModel.toggleItem(item) },
                                        onDelete = { viewModel.deleteItem(item.id) }
                                    )
                                    if (index < shoppingState.completedItems.size - 1) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(1.dp)
                                                .background(BorderLight.copy(alpha = 0.6f))
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }

    // Confirmation Dialog for "Borrar todo"
    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.setShowClearDialog(false) },
            title = {
                Text(
                    text = "¿Eliminar toda la lista?",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
            },
            text = {
                Text(
                    text = "Esta acción borrará todos los ingredientes pendientes y completados de la lista de compras.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                )
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.clearAll() },
                    colors = ButtonDefaults.buttonColors(containerColor = TerracottaRed)
                ) {
                    Text("Borrar todo")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.setShowClearDialog(false) }) {
                    Text("Cancelar", color = TextSecondary)
                }
            },
            containerColor = CardSurface,
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
private fun ShoppingItemRow(
    item: ShoppingItemEntity,
    onToggle: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .testTag("shopping_item_${item.id}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Custom Checkbox
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(6.dp))
                .border(
                    width = 1.8.dp,
                    color = if (item.isCompleted) TerracottaRed else TextTertiary,
                    shape = RoundedCornerShape(6.dp)
                )
                .background(if (item.isCompleted) TerracottaRed else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            if (item.isCompleted) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Completado",
                    tint = Color.White,
                    modifier = Modifier.size(17.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        // Name
        Text(
            text = item.name,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = if (item.isCompleted) FontWeight.Normal else FontWeight.Medium,
                fontSize = 15.sp,
                color = if (item.isCompleted) TextTertiary else TextPrimary,
                textDecoration = if (item.isCompleted) TextDecoration.LineThrough else null
            ),
            modifier = Modifier.weight(1f)
        )

        // Quantity (if any)
        if (item.quantity.isNotBlank()) {
            Text(
                text = item.quantity,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 14.sp,
                    color = if (item.isCompleted) TextTertiary else TextSecondary
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        // Delete IconButton
        IconButton(
            onClick = onDelete,
            modifier = Modifier.size(28.dp)
        ) {
            Icon(
                imageVector = Icons.Default.DeleteOutline,
                contentDescription = "Eliminar",
                tint = TextTertiary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
