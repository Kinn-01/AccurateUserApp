package com.example.accurateuserapp.presentation.adduser

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.accurateuserapp.R
import com.example.accurateuserapp.ui.theme.RoyalBlue
import com.example.accurateuserapp.ui.theme.Turquoise

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUserScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddUserViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()
    var cityMenuExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.add_new_user),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Full Name Field
            OutlinedTextField(
                value = state.name,
                onValueChange = { viewModel.onNameChange(it) },
                label = { Text(stringResource(R.string.full_name)) },
                placeholder = { Text(stringResource(R.string.placeholder_full_name)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null
                    )
                },
                isError = state.nameError != null,
                supportingText = state.nameError?.let { { Text(it) } },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words
                ),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Gender Selector Card Group (Extremely Premium UI element!)
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(R.string.gender),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Male Card selection
                    val isMaleSelected = state.gender == 0
                    OutlinedCard(
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(
                            width = if (isMaleSelected) 2.dp else 1.dp,
                            color = if (isMaleSelected) RoyalBlue else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewModel.onGenderChange(0) }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Male,
                                contentDescription = null,
                                tint = if (isMaleSelected) RoyalBlue else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = stringResource(R.string.male),
                                fontWeight = if (isMaleSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isMaleSelected) RoyalBlue else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }

                    // Female Card selection
                    val isFemaleSelected = state.gender == 1
                    OutlinedCard(
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(
                            width = if (isFemaleSelected) 2.dp else 1.dp,
                            color = if (isFemaleSelected) Turquoise else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewModel.onGenderChange(1) }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Female,
                                contentDescription = null,
                                tint = if (isFemaleSelected) Turquoise else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = stringResource(R.string.female),
                                fontWeight = if (isFemaleSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isFemaleSelected) Turquoise else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Address Field
            OutlinedTextField(
                value = state.address,
                onValueChange = { viewModel.onAddressChange(it) },
                label = { Text(stringResource(R.string.address)) },
                placeholder = { Text(stringResource(R.string.placeholder_address)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null
                    )
                },
                isError = state.addressError != null,
                supportingText = state.addressError?.let { { Text(it) } },
                maxLines = 3,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // City dropdown selection using ExposedDropdownMenuBox (Highly native M3!)
            ExposedDropdownMenuBox(
                expanded = cityMenuExpanded,
                onExpandedChange = { cityMenuExpanded = it }
            ) {
                OutlinedTextField(
                    value = state.city,
                    onValueChange = { viewModel.onCityChange(it) },
                    readOnly = false,
                    label = { Text(stringResource(R.string.city)) },
                    placeholder = { Text(stringResource(R.string.placeholder_city)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.LocationCity,
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = cityMenuExpanded)
                    },
                    isError = state.cityError != null,
                    supportingText = state.cityError?.let { { Text(it) } },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                val filteredCities = state.cities.filter {
                    it.contains(state.city, ignoreCase = true)
                }

                if (filteredCities.isNotEmpty()) {
                    ExposedDropdownMenu(
                        expanded = cityMenuExpanded,
                        onDismissRequest = { cityMenuExpanded = false }
                    ) {
                        filteredCities.forEach { cityName ->
                            DropdownMenuItem(
                                text = { Text(cityName) },
                                onClick = {
                                    viewModel.onCityChange(cityName)
                                    cityMenuExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Email Field
            OutlinedTextField(
                value = state.email,
                onValueChange = { viewModel.onEmailChange(it) },
                label = { Text(stringResource(R.string.email)) },
                placeholder = { Text(stringResource(R.string.placeholder_email)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null
                    )
                },
                isError = state.emailError != null,
                supportingText = state.emailError?.let { { Text(it) } },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Phone Number Field
            OutlinedTextField(
                value = state.phoneNumber,
                onValueChange = { viewModel.onPhoneNumberChange(it) },
                label = { Text(stringResource(R.string.telephone_number)) },
                placeholder = { Text(stringResource(R.string.placeholder_telephone_number)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null
                    )
                },
                isError = state.phoneNumberError != null,
                supportingText = state.phoneNumberError?.let { { Text(it) } },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone
                ),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Submit Button
            Button(
                onClick = { viewModel.submitForm() },
                enabled = !state.isLoading,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = stringResource(R.string.save_user),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // General Error Message
            state.generalError?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}