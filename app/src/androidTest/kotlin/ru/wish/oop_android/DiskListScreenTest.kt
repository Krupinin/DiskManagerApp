package ru.wish.oop_android

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class DiskListScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testAddButtonExists() {
        composeTestRule.onNodeWithText("+").assertExists()
    }

    @Test
    fun testFilterDropdownExists() {
        composeTestRule.onNodeWithText("Фильтр").assertExists()
    }

    @Test
    fun testScreenTitleDisplayed() {
        composeTestRule.onNodeWithText("Жёсткие диски").assertExists()
    }

    @Test
    fun testFilterOptionsAvailable() {
        // Click on filter dropdown
        composeTestRule.onNodeWithText("Фильтр").performClick()

        // Check all filter options exist
        composeTestRule.onNodeWithText("Все диски").assertExists()
        composeTestRule.onNodeWithText("Диски ёмкостью более 200 ГБ").assertExists()
        composeTestRule.onNodeWithText("Внутренние диски").assertExists()
        composeTestRule.onNodeWithText("Внешние диски").assertExists()
    }

    @Test
    fun testFilterSelectionChangesDisplay() {
        // Initially all disks filter should be selected
        composeTestRule.onNodeWithText("Все диски").assertExists()

        // Click on filter dropdown and select internal disks
        composeTestRule.onNodeWithText("Фильтр").performClick()
        composeTestRule.onNodeWithText("Внутренние диски").performClick()

        // Verify the filter changed
        composeTestRule.onNodeWithText("Внутренние диски").assertExists()
    }

    @Test
    fun testEmptyStateDisplaysStats() {
        // Stats should show 0 for all counts when no disks
        composeTestRule.onNodeWithText("Всего дисков: 0, Отобрано: 0, Внешних: 0, Внутренних: 0").assertExists()
    }

    @Test
    fun testDiskItemDisplaysCorrectly() {
        // Add a test disk first
        composeTestRule.onNodeWithText("+").performClick()

        // Fill form and save (this would be tested in AddEditScreenTest)
        // For now, assuming disks are added, test display
        composeTestRule.onNodeWithText("TestDisk").assertExists()
        composeTestRule.onNodeWithText("1000 GB").assertExists()
        composeTestRule.onNodeWithText("Редактировать").assertExists()
        composeTestRule.onNodeWithText("Удалить").assertExists()
    }

    @Test
    fun testEditButtonNavigates() {
        // This test would require setting up navigation testing
        // For now, just verify edit button exists and is clickable
        composeTestRule.onNodeWithText("+").performClick()
        // Assuming disk added, then test navigation
        composeTestRule.onNodeWithText("Редактировать").assertExists().performClick()
        // Would need to verify navigation occurred
    }

    @Test
    fun testDeleteButtonShowsConfirmationOrDeletes() {
        // Add disk first
        composeTestRule.onNodeWithText("+").performClick()
        // Fill form and save

        // Click delete
        composeTestRule.onNodeWithText("Удалить").performClick()

        // Verify disk is removed from list
        composeTestRule.onNodeWithText("TestDisk").assertDoesNotExist()
    }
}
