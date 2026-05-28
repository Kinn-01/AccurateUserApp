package com.example.accurateuserapp.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.accurateuserapp.R
import com.example.accurateuserapp.domain.model.SortOrder

@Composable
fun SortMenu(
    selectedSortOrder: SortOrder,
    onSortOrderSelected: (SortOrder) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.padding(end = 16.dp)) {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Default.Sort,
                contentDescription = stringResource(R.string.sort)
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.default)) },
                onClick = {
                    onSortOrderSelected(SortOrder.NONE)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.name_asc)) },
                onClick = {
                    onSortOrderSelected(SortOrder.NAME_ASC)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.name_desc)) },
                onClick = {
                    onSortOrderSelected(SortOrder.NAME_DESC)
                    expanded = false
                }
            )
        }
    }
}