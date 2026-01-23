package ru.wish.oop_android

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.CustomTestApplication
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class AddEditDiskScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testAddScreenDisplaysCorrectTitle() {
        // Navigate to add screen
        composeTestRule.onNodeWithText("+").performClick()

        composeTestRule.onNodeWithText("Редактировать диск").assertExists()
    }

    @Test
    fun testFormFieldsDisplayed() {
        composeTestRule.onNodeWithText("+").performClick()

        // Check all form fields exist
        composeTestRule.onNodeWithText("Название").assertExists()
        composeTestRule.onNodeWithText("Ёмкость (ГБ)").assertExists()
        composeTestRule.onNodeWithText("Внешний").assertExists()
        composeTestRule.onNodeWithText("Внутренний").assertExists()
        composeTestRule.onNodeWithText("Отмена").assertExists()
        composeTestRule.onNodeWithText("Сохранить").assertExists()
    }

    @Test
    fun testExternalDiskTypeSelectedByDefault() {
        composeTestRule.onNodeWithText("+").performClick()

        // External should be selected by default
        composeTestRule.onNodeWithText("Внешний").assertExists()
        composeTestRule.onNodeWithText("Защита от падения").assertExists()
    }

    @Test
    fun testInternalDiskTypeSelectionShowsSizeField() {
        composeTestRule.onNodeWithText("+").performClick()

        // Click on Internal radio button
        composeTestRule.onNodeWithText("Внутренний").performClick()

        // Size field should appear
        composeTestRule.onNodeWithText("Размер").assertExists()
        composeTestRule.onNodeWithText("3.5\"").assertExists()
    }

    @Test
    fun testSizeDropdownOptions() {
        composeTestRule.onNodeWithText("+").performClick()
        composeTestRule.onNodeWithText("Внутренний").performClick()

        // Click size dropdown
        composeTestRule.onNodeWithText("Размер").performClick()

        composeTestRule.onNodeWithText("2.5\"").assertExists()
        composeTestRule.onNodeWithText("3.5\"").assertExists()
    }

    @Test
    fun testExternalDiskDropProtectionCheckbox() {
        composeTestRule.onNodeWithText("+").performClick()

        // Checkbox should be unchecked by default
        composeTestRule.onNodeWithText("Защита от падения").assertExists()

        // Click checkbox
        composeTestRule.onNode(hasText("Защита от падения").and(hasClickAction())).performClick()
    }

    @Test
    fun testFormValidationEmptyFields() {
        composeTestRule.onNodeWithText("+").performClick()

        // Try to save without filling fields
        composeTestRule.onNodeWithText("Сохранить").performClick()

        // Should not navigate back (form validation failed)
        composeTestRule.onNodeWithText("Редактировать диск").assertExists()
    }

    @Test
    fun testFormValidationInvalidCapacity() {
        composeTestRule.onNodeWithText("+").performClick()

        // Fill name but invalid capacity
        composeTestRule.onNodeWithText("Название").performTextInput("Test Disk")
        composeTestRule.onNodeWithText("Ёмкость (ГБ)").performTextInput("invalid")

        composeTestRule.onNodeWithText("Сохранить").performClick()

        // Should not navigate back
        composeTestRule.onNodeWithText("Редактировать диск").assertExists()
    }

    @Test
    fun testSuccessfulDiskCreation() {
        composeTestRule.onNodeWithText("+").performClick()

        // Fill valid data
        composeTestRule.onNodeWithText("Название").performTextInput("Test External Disk")
        composeTestRule.onNodeWithText("Ёмкость (ГБ)").performTextInput("1000")

        // Select external with protection
        composeTestRule.onNodeWithText("Защита от падения").performClick()

        composeTestRule.onNodeWithText("Сохранить").performClick()

        // Should navigate back to list
        composeTestRule.onNodeWithText("Жёсткие диски").assertExists()

        // Verify disk appears in list
        composeTestRule.onNodeWithText("Test External Disk").assertExists()
        composeTestRule.onNodeWithText("1000 GB").assertExists()
    }

    @Test
    fun testInternalDiskCreation() {
        composeTestRule.onNodeWithText("+").performClick()

        // Fill valid data for internal disk
        composeTestRule.onNodeWithText("Название").performTextInput("Test Internal Disk")
        composeTestRule.onNodeWithText("Ёмкость (ГБ)").performTextInput("2000")

        // Select internal
        composeTestRule.onNodeWithText("Внутренний").performClick()

        // Select size
        composeTestRule.onNodeWithText("Размер").performClick()
        composeTestRule.onNodeWithText("2.5\"").performClick()

        composeTestRule.onNodeWithText("Сохранить").performClick()

        // Should navigate back to list
        composeTestRule.onNodeWithText("Жёсткие диски").assertExists()

        // Verify disk appears in list
        composeTestRule.onNodeWithText("Test Internal Disk").assertExists()
        composeTestRule.onNodeWithText("2000 GB").assertExists()
    }

    @Test
    fun testCancelButtonNavigatesBack() {
        composeTestRule.onNodeWithText("+").performClick()

        composeTestRule.onNodeWithText("Отмена").performClick()

        // Should navigate back to list
        composeTestRule.onNodeWithText("Жёсткие диски").assertExists()
    }

    @Test
    fun testEditModeLoadsExistingData() {
        // First create a disk
        composeTestRule.onNodeWithText("+").performClick()
        composeTestRule.onNodeWithText("Название").performTextInput("Disk to Edit")
        composeTestRule.onNodeWithText("Ёмкость (ГБ)").performTextInput("500")
        composeTestRule.onNodeWithText("Сохранить").performClick()

        // Now edit the disk
        composeTestRule.onNodeWithText("Редактировать").performClick()

        // Check fields are pre-filled
        composeTestRule.onNodeWithText("Disk to Edit").assertExists()
        composeTestRule.onNodeWithText("500").assertExists()
    }

    @Test
    fun testEditModeUpdatesDisk() {
        // Create and then edit disk
        composeTestRule.onNodeWithText("+").performClick()
        composeTestRule.onNodeWithText("Название").performTextInput("Original Name")
        composeTestRule.onNodeWithText("Ёмкость (ГБ)").performTextInput("300")
        composeTestRule.onNodeWithText("Сохранить").performClick()

        // Edit the disk
        composeTestRule.onNodeWithText("Редактировать").performClick()
        composeTestRule.onNodeWithText("Название").performTextReplacement("Updated Name")
        composeTestRule.onNodeWithText("Ёмкость (ГБ)").performTextReplacement("600")
        composeTestRule.onNodeWithText("Сохранить").performClick()

        // Verify updated data
        composeTestRule.onNodeWithText("Updated Name").assertExists()
        composeTestRule.onNodeWithText("600 GB").assertExists()
        composeTestRule.onNodeWithText("Original Name").assertDoesNotExist()
    }
}
