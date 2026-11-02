package com.Qerxavi7.Nomlut.presentation.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.Qerxavi7.Nomlut.R
import com.Qerxavi7.Nomlut.core.theme.Accent
import com.Qerxavi7.Nomlut.core.theme.SecondaryRose
import com.Qerxavi7.Nomlut.core.theme.SheetColor
import com.Qerxavi7.Nomlut.core.theme.TextPrimary
import com.Qerxavi7.Nomlut.core.theme.TextSecondary
import androidx.compose.ui.res.stringResource

@Composable
fun PauseDialog(
    scoreText: String,
    moves: Int,
    onResume: () -> Unit,
    onRestart: () -> Unit,
    onMenu: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onResume,
        shape = RoundedCornerShape(24.dp),
        containerColor = SheetColor,
        titleContentColor = TextPrimary,
        textContentColor = TextSecondary,
        title = {
            Text(
                text = stringResource(R.string.paused),
                style = MaterialTheme.typography.titleLarge,
                color = Accent
            )
        },
        text = {
            Text(
                text = stringResource(R.string.pause_body, scoreText, moves),
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        },
        confirmButton = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                TextButton(onClick = onResume) {
                    Text(
                        text = stringResource(R.string.resume),
                        style = MaterialTheme.typography.labelLarge,
                        color = Accent
                    )
                }
                TextButton(onClick = onRestart) {
                    Text(
                        text = stringResource(R.string.restart),
                        style = MaterialTheme.typography.labelLarge,
                        color = TextPrimary
                    )
                }
                TextButton(onClick = onMenu) {
                    Text(
                        text = stringResource(R.string.menu),
                        style = MaterialTheme.typography.labelLarge,
                        color = SecondaryRose
                    )
                }
            }
        }
    )
}
