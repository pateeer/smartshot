package com.example.shottracker

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.shottracker.data.Drink
import com.example.shottracker.data.DrinkEvent
import com.example.shottracker.data.Gender
import com.example.shottracker.data.Person
import com.example.shottracker.data.Settings
import java.time.LocalDateTime

class MainViewModel : ViewModel() {
    
    private val _people = mutableStateListOf<Person>()
    val people: List<Person> = _people
    
    private val _selectedDrink = mutableStateOf(Drink.PRESETS[2]) // Default to Shot
    val selectedDrink = _selectedDrink
    
    private val _showAddPersonDialog = mutableStateOf(false)
    val showAddPersonDialog = _showAddPersonDialog
    
    private val _settings = mutableStateOf(Settings())
    val settings = _settings
    
    // Session tracking
    private val _drinkHistory = mutableStateListOf<DrinkEvent>()
    val drinkHistory: List<DrinkEvent> = _drinkHistory
    
    private val _sessionStartTime = mutableStateOf<LocalDateTime?>(null)
    val sessionStartTime = _sessionStartTime
    
    // Session stats
    val totalShots: Int
        get() = _people.sumOf { it.shotCount }
    
    val totalVolumeMl: Int
        get() = _drinkHistory.sumOf { it.drink.volumeMl }
    
    val totalAlcoholGrams: Double
        get() = _drinkHistory.sumOf { it.drink.alcoholGrams }
    
    fun addPerson(name: String, heightCm: Int, weightKg: Double, gender: Gender) {
        _people.add(
            Person(
                name = name,
                heightCm = heightCm,
                weightKg = weightKg,
                gender = gender
            )
        )
    }
    
    fun removePerson(personId: String) {
        _people.removeIf { it.id == personId }
        // Also remove their drink events from history
        _drinkHistory.removeIf { it.personId == personId }
    }
    
    fun incrementShots(personId: String) {
        val index = _people.indexOfFirst { it.id == personId }
        if (index >= 0) {
            val person = _people[index]
            _people[index] = person.copy(shotCount = person.shotCount + 1)
            
            // Start session if first drink
            if (_sessionStartTime.value == null) {
                _sessionStartTime.value = LocalDateTime.now()
            }
            
            // Record drink event
            _drinkHistory.add(
                DrinkEvent(
                    personId = personId,
                    personName = person.name,
                    drink = _selectedDrink.value
                )
            )
        }
    }
    
    fun decrementShots(personId: String) {
        val index = _people.indexOfFirst { it.id == personId }
        if (index >= 0 && _people[index].shotCount > 0) {
            _people[index] = _people[index].copy(shotCount = _people[index].shotCount - 1)
            
            // Remove last drink event for this person
            val lastEvent = _drinkHistory.lastOrNull { it.personId == personId }
            if (lastEvent != null) {
                _drinkHistory.remove(lastEvent)
            }
        }
    }
    
    fun selectDrink(drink: Drink) {
        _selectedDrink.value = drink
    }
    
    fun showAddDialog() {
        _showAddPersonDialog.value = true
    }
    
    fun hideAddDialog() {
        _showAddPersonDialog.value = false
    }
    
    fun updateSettings(newSettings: Settings) {
        _settings.value = newSettings
    }
    
    fun resetSession() {
        _drinkHistory.clear()
        _people.replaceAll { it.copy(shotCount = 0) }
        _sessionStartTime.value = null
    }
}
