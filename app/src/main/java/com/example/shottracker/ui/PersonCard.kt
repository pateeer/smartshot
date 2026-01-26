package com.example.shottracker.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shottracker.data.Drink
import com.example.shottracker.data.Person
import com.example.shottracker.ui.theme.*
import com.example.shottracker.util.BacCalculator

@Composable
fun PersonCard(
    person: Person,
    drink: Drink,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bacResult = BacCalculator.calculate(person, drink)
    
    val statusColor = when (bacResult.intoxicationLevel) {
        BacCalculator.IntoxicationLevel.SOBER -> StatusSober
        BacCalculator.IntoxicationLevel.BUZZED -> StatusBuzzed
        BacCalculator.IntoxicationLevel.TIPSY -> StatusTipsy
        BacCalculator.IntoxicationLevel.DRUNK -> StatusDrunk
        BacCalculator.IntoxicationLevel.VERY_DRUNK -> StatusVeryDrunk
        BacCalculator.IntoxicationLevel.DANGEROUS -> StatusDangerous
    }
    
    // Animated count
    var displayCount by remember { mutableIntStateOf(person.shotCount) }
    LaunchedEffect(person.shotCount) {
        displayCount = person.shotCount
    }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(SurfaceCard, SurfaceCardElevated)
                    )
                )
                .padding(16.dp)
        ) {
            // Remove button
            IconButton(
                onClick = onRemove,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove",
                    tint = TextMuted,
                    modifier = Modifier.size(16.dp)
                )
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left side - Person info
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = person.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = "${person.heightCm}cm • ${person.weightKg.toInt()}kg • ${person.gender.displayName}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // BAC info
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Status indicator
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(statusColor)
                        )
                        
                        Column {
                            Text(
                                text = BacCalculator.formatBac(bacResult.bac),
                                style = MaterialTheme.typography.titleMedium,
                                color = statusColor,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = bacResult.intoxicationLevel.displayName,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }
                    }
                    
                    if (bacResult.bac > 0) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "⏱ ${BacCalculator.formatTimeUntilSober(bacResult.hoursUntilSober)} do vytriezvenia",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted
                        )
                    }
                }
                
                // Right side - Counter
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Decrement button
                    IconButton(
                        onClick = onDecrement,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(DecrementButton.copy(alpha = 0.2f)),
                        enabled = person.shotCount > 0
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Remove shot",
                            tint = if (person.shotCount > 0) DecrementButton else TextMuted
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Count display
                    AnimatedContent(
                        targetState = displayCount,
                        transitionSpec = {
                            if (targetState > initialState) {
                                slideInVertically { -it } + fadeIn() togetherWith
                                    slideOutVertically { it } + fadeOut()
                            } else {
                                slideInVertically { it } + fadeIn() togetherWith
                                    slideOutVertically { -it } + fadeOut()
                            }.using(SizeTransform(clip = false))
                        },
                        label = "count_animation"
                    ) { count ->
                        Text(
                            text = count.toString(),
                            style = MaterialTheme.typography.displaySmall,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 36.sp
                        )
                    }
                    
                    Text(
                        text = "poháre",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Increment button
                    IconButton(
                        onClick = onIncrement,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(IncrementButton.copy(alpha = 0.2f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add shot",
                            tint = IncrementButton
                        )
                    }
                }
            }
        }
    }
}
