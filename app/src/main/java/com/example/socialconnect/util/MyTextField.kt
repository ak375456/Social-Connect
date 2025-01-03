package com.example.socialconnect.util

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Eye
import com.composables.icons.lucide.EyeOff
import com.composables.icons.lucide.Lucide

@Composable
fun MyTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    label: String,
    painter: ImageVector,
    isPassword: Boolean = false,
    passwordVisible: MutableState<Boolean>? = null,
    onPasswordToggle: (() -> Unit)? = null,
    isError:Boolean = false,
    supportingTextFunction : @Composable (() -> Unit)? = null,
    isReadOnly: Boolean = false
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        value = value,
        onValueChange = onValueChanged,
        label = { Text(label) },
        leadingIcon = {
            Icon(imageVector = painter, contentDescription = null)
        },
        readOnly = isReadOnly,
        trailingIcon = {
            if (isPassword) {
                IconButton(onClick = { onPasswordToggle?.invoke() }) {
                    passwordVisible?.let {
                        Icon(
                            imageVector = if (it.value) Lucide.Eye else Lucide.EyeOff,
                            contentDescription = if (passwordVisible.value) "Hide password" else "Show password"
                        )
                    }
                }
            }
        },
        visualTransformation = if (isPassword && !passwordVisible?.value!!) PasswordVisualTransformation() else VisualTransformation.None,
        isError = isError,
        supportingText = supportingTextFunction,

    )
}
