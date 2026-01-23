package ru.wish.oop_android

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.CustomTestApplication
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testFullAddDiskFlow() {
        // Start on disk list screen
        composeTestRule.onNodeWithText("Жёсткие диски").assertExists()

        // Navigate to add screen
        composeTestRule.onNodeWithText("+").performClick()
        composeTestRule.onNodeWithText("Редактировать диск").assertExists()

        // Fill form and save
        composeTestRule.onNodeWithText("Название").performTextInput("Test Disk")
        composeTestRule.onNodeWithText("Ёмкость (ГБ)").performTextInput("1000")
        composeTestRule.onNodeWithText("Сохранить").performClick()

        // Should be back on list screen with new disk
        composeTestRule.onNodeWithText("Жёсткие диски").assertExists()
        composeTestRule.onNodeWithText("Test Disk").assertExists()
        composeTestRule.onNodeWithText("1000 GB").assertExists()
    }

    @Test
    fun testEditDiskFlow() {
        // Add a disk first
        composeTestRule.onNodeWithText("+").performClick()
        composeTestRule.onNodeWithText("Название").performTextInput("Original Disk")
        composeTestRule.onNodeWithText("Ёмкость (ГБ)").performTextInput("500")
        composeTestRule.onNodeWithText("Сохранить").performClick()

        // Edit the disk
        composeTestRule.onNodeWithText("Редактировать").performClick()
        composeTestRule.onNodeWithText("Редактировать диск").assertExists()

        // Modify and save
        composeTestRule.onNodeWithText("Название").performTextReplacement("Edited Disk")
        composeTestRule.onNodeWithText("Ёмкость (ГБ)").performTextReplacement("750")
        composeTestRule.onNodeWithText("Сохранить").performClick()

        // Verify changes
        composeTestRule.onNodeWithText("Жёсткие диски").assertExists()
        composeTestRule.onNodeWithText("Edited Disk").assertExists()
        composeTestRule.onNodeWithText("750 GB").assertExists()
        composeTestRule.onNodeWithText("Original Disk").assertDoesNotExist()
    }

    @Test
    fun testCancelAddDisk() {
        // Navigate to add screen
        composeTestRule.onNodeWithText("+").performClick()
        composeTestRule.onNodeWithText("Редактировать диск").assertExists()

        // Fill form but cancel
        composeTestRule.onNodeWithText("Название").performTextInput("Cancelled Disk")
        composeTestRule.onNodeWithText("Отмена").performClick()

        // Should be back on list screen without new disk
        composeTestRule.onNodeWithText("Жёсткие диски").assertExists()
        composeTestRule.onNodeWithText("Cancelled Disk").assertDoesNotExist()
    }

    @Test
    fun testDeleteDiskFlow() {
        // Add a disk
        composeTestRule.onNodeWithText("+").performClick()
        composeTestRule.onNodeWithText("Название").performTextInput("Disk to Delete")
        composeTestRule.onNodeWithText("Ёмкость (ГБ)").performTextInput("250")
        composeTestRule.onNodeWithText("Сохранить").performClick()

        // Verify disk exists
        composeTestRule.onNodeWithText("Disk to Delete").assertExists()

        // Delete the disk
        composeTestRule.onNodeWithText("Удалить").performClick()

        // Verify disk is gone
        composeTestRule.onNodeWithText("Disk to Delete").assertDoesNotExist()
    }

    @Test
    fun testFilterAndNavigation() {
        // Add different types of disks
        // Add external disk
        composeTestRule.onNodeWithText("+").performClick()
        composeTestRule.onNodeWithText("Название").performTextInput("External Disk")
        composeTestRule.onNodeWithText("Ёмкость (ГБ)").performTextInput("300")
        composeTestRule.onNodeWithText("Сохранить").performClick()

        // Add internal disk
        composeTestRule.onNodeWithText("+").performClick()
        composeTestRule.onNodeWithText("Название").performTextInput("Internal Disk")
        composeTestRule.onNodeWithText("Ёмкость (ГБ)").performTextInput("400")
        composeTestRule.onNodeWithText("Внутренний").performClick()
        composeTestRule.onNodeWithText("Сохранить").performClick()

        // Apply external filter
        composeTestRule.onNodeWithText("Фильтр").performClick()
        composeTestRule.onNodeWithText("Внешние диски").performClick()

        // Only external disk should be visible
        composeTestRule.onNodeWithText("External Disk").assertExists()
        composeTestRule.onNodeWithText("Internal Disk").assertDoesNotExist()

        // Edit external disk
        composeTestRule.onNodeWithText("Редактировать").performClick()
        composeTestRule.onNodeWithText("Название").performTextReplacement("Edited External")
        composeTestRule.onNodeWithText("Сохранить").performClick()

        // Should still show edited disk after filter
        composeTestRule.onNodeWithText("Edited External").assertExists()
    }

    @Test
    fun testBackNavigationFromEdit() {
        // Add disk
        composeTestRule.onNodeWithText("+").performClick()
        composeTestRule.onNodeWithText("Название").performTextInput("Test Disk")
        composeTestRule.onNodeWithText("Ёмкость (ГБ)").performTextInput("100")
        composeTestRule.onNodeWithText("Сохранить").performClick()

        // Edit disk
        composeTestRule.onNodeWithText("Редактировать").performClick()
        composeTestRule.onNodeWithText("Редактировать диск").assertExists()

        // Cancel edit
        composeTestRule.onNodeWithText("Отмена").performClick()

        // Should be back on list
        composeTestRule.onNodeWithText("Жёсткие диски").assertExists()
        composeTestRule.onNodeWithText("Test Disk").assertExists()
    }

    @Test
    fun testMultipleDiskOperations() {
        // Add multiple disks
        addDisk("Disk 1", "100")
        addDisk("Disk 2", "200")
        addDisk("Disk 3", "300")

        // Verify all disks exist
        composeTestRule.onNodeWithText("Disk 1").assertExists()
        composeTestRule.onNodeWithText("Disk 2").assertExists()
        composeTestRule.onNodeWithText("Disk 3").assertExists()

        // Edit first disk
        composeTestRule.onAllNodesWithText("Редактировать")[0].performClick()
        composeTestRule.onNodeWithText("Название").performTextReplacement("Edited Disk 1")
        composeTestRule.onNodeWithText("Сохранить").performClick()

        // Delete second disk
        composeTestRule.onAllNodesWithText("Удалить")[0].performClick()

        // Verify final state
        composeTestRule.onNodeWithText("Edited Disk 1").assertExists()
        composeTestRule.onNodeWithText("Disk 2").assertDoesNotExist()
        composeTestRule.onNodeWithText("Disk 3").assertExists()
    }

    private fun addDisk(name: String, capacity: String) {
        composeTestRule.onNodeWithText("+").performClick()
        composeTestRule.onNodeWithText("Название").performTextInput(name)
        composeTestRule.onNodeWithText("Ёмкость (ГБ)").performTextInput(capacity)
        composeTestRule.onNodeWithText("Сохранить").performClick()
    }
}
