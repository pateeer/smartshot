package com.example.shottracker.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocalBar
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.shottracker.MainViewModel
import com.example.shottracker.data.Drink
import com.example.shottracker.ui.theme.*

@Composable
fun ShotTrackerScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val people = viewModel.people
    val selectedDrink by viewModel.selectedDrink
    val showAddDialog by viewModel.showAddPersonDialog
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(GradientStart, GradientMid, GradientEnd)
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp, start = 24.dp, end = 24.dp, bottom = 16.dp)
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalBar,
                            contentDescription = null,
                            tint = Primary,
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            text = "SmartShot",
                            style = MaterialTheme.typography.headlineLarge,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = "Sledujte svoje pitie zodpovedne",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextMuted
                    )
                }
            }
            
            // Drink selector
            Column(
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "Vyberte typ nápoja",
                    style = MaterialTheme.typography.labelLarge,
                    color = TextSecondary
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(Drink.PRESETS) { drink ->
                        DrinkChip(
                            drink = drink,
                            isSelected = drink == selectedDrink,
                            onClick = { viewModel.selectDrink(drink) }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Selected drink info
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = SurfaceCard.copy(alpha = 0.5f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${selectedDrink.alcoholPercentage}%",
                                style = MaterialTheme.typography.titleMedium,
                                color = Secondary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Alkohol",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${selectedDrink.volumeMl}ml",
                                style = MaterialTheme.typography.titleMedium,
                                color = Secondary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Objem",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = String.format("%.1fg", selectedDrink.alcoholGrams),
                                style = MaterialTheme.typography.titleMedium,
                                color = Secondary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Čistý alkohol",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // People list
            if (people.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Group,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Nikto tu ešte nie je!",
                            style = MaterialTheme.typography.titleLarge,
                            color = TextSecondary
                        )
                        Text(
                            text = "Ťuknite na + a pridajte osobu",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMuted
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(people, key = { it.id }) { person ->
                        PersonCard(
                            person = person,
                            drink = selectedDrink,
                            onIncrement = { viewModel.incrementShots(person.id) },
                            onDecrement = { viewModel.decrementShots(person.id) },
                            onRemove = { viewModel.removePerson(person.id) },
                            modifier = Modifier.animateItem()
                        )
                    }
                }
            }
        }
        
        // FAB
        FloatingActionButton(
            onClick = { viewModel.showAddDialog() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            containerColor = Primary,
            contentColor = TextPrimary,
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add person"
            )
        }
        
        // Add person dialog
        if (showAddDialog) {
            AddPersonDialog(
                onDismiss = { viewModel.hideAddDialog() },
                onConfirm = { name, height, weight, gender ->
                    viewModel.addPerson(name, height, weight, gender)
                    viewModel.hideAddDialog()
                }
            )
        }
    }
}

@Composable
private fun DrinkChip(
    drink: Drink,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = drink.icon,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Text(drink.name)
            }
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Primary,
            selectedLabelColor = TextPrimary,
            containerColor = SurfaceCard,
            labelColor = TextSecondary
        ),
        border = FilterChipDefaults.filterChipBorder(
            borderColor = TextMuted.copy(alpha = 0.3f),
            selectedBorderColor = Primary,
            enabled = true,
            selected = isSelected
        )
    )
}
