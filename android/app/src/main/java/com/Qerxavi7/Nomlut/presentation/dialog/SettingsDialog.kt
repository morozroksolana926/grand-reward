package com.Qerxavi7.Nomlut.presentation.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.Qerxavi7.Nomlut.R
import com.Qerxavi7.Nomlut.core.theme.Accent
import com.Qerxavi7.Nomlut.core.theme.BackgroundMain
import com.Qerxavi7.Nomlut.core.theme.GlassFill
import com.Qerxavi7.Nomlut.core.theme.Primary
import com.Qerxavi7.Nomlut.core.theme.SheetColor
import com.Qerxavi7.Nomlut.core.theme.TextMuted
import com.Qerxavi7.Nomlut.core.theme.TextPrimary
import com.Qerxavi7.Nomlut.core.theme.TextSecondary

@Composable
fun SettingsDialog(
    soundCues: Boolean,
    haptics: Boolean,
    reducedMotion: Boolean,
    onSoundCues: (Boolean) -> Unit,
    onHaptics: (Boolean) -> Unit,
    onReducedMotion: (Boolean) -> Unit,
    onResetRequest: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        containerColor = SheetColor,
        titleContentColor = TextPrimary,
        textContentColor = TextSecondary,
        title = {
            Text(
                text = stringResource(R.string.settings),
                style = MaterialTheme.typography.titleLarge,
                color = Accent
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                SettingRow(
                    label = stringResource(R.string.settings_sound),
                    checked = soundCues,
                    onCheckedChange = onSoundCues
                )
                SettingRow(
                    label = stringResource(R.string.settings_haptics),
                    checked = haptics,
                    onCheckedChange = onHaptics
                )
                SettingRow(
                    label = stringResource(R.string.settings_reduced_motion),
                    checked = reducedMotion,
                    onCheckedChange = onReducedMotion
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.resume),
                    style = MaterialTheme.typography.labelLarge,
                    color = Accent
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onResetRequest) {
                Text(
                    text = stringResource(R.string.reset_progress),
                    style = MaterialTheme.typography.labelLarge,
                    color = Primary
                )
            }
        }
    )
}

@Composable
private fun SettingRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val state = stringResource(if (checked) R.string.setting_on else R.string.setting_off)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .semantics { stateDescription = state },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = TextPrimary
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = BackgroundMain,
                checkedTrackColor = Accent,
                uncheckedThumbColor = TextMuted,
                uncheckedTrackColor = GlassFill
            )
        )
    }
}
