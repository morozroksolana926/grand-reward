package com.Qerxavi7.Nomlut.core.ui

import androidx.compose.ui.graphics.Color
import com.Qerxavi7.Nomlut.R
import com.Qerxavi7.Nomlut.core.theme.Accent
import com.Qerxavi7.Nomlut.core.theme.Primary
import com.Qerxavi7.Nomlut.core.theme.SecondaryRose
import com.Qerxavi7.Nomlut.core.theme.TextPrimary
import com.Qerxavi7.Nomlut.core.theme.TokenAzure
import com.Qerxavi7.Nomlut.domain.model.TokenType

data class TokenVisual(
    val spriteRes: Int,
    val tint: Color,
    val labelRes: Int
)

fun visualFor(type: TokenType): TokenVisual = when (type) {
    TokenType.GOLD_INGOT -> TokenVisual(R.drawable.sprite_1, Accent, R.string.token_gold_ingot)
    TokenType.RUBY_SEAL -> TokenVisual(R.drawable.sprite_2, Primary, R.string.token_ruby_seal)
    TokenType.ROSE_GEM -> TokenVisual(R.drawable.sprite_3, SecondaryRose, R.string.token_rose_gem)
    TokenType.IVORY_COIN -> TokenVisual(R.drawable.sprite_4, TextPrimary, R.string.token_ivory_coin)
    TokenType.AZURE_CREST -> TokenVisual(R.drawable.sprite_5, TokenAzure, R.string.token_azure_crest)
}
