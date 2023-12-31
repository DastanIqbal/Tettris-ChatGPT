package com.dastanapps.games

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dastanapps.games.models.GameInfo
import com.dastanapps.games.ui.theme.GameAppTheme
import com.dastanapps.opengles.tuts.OpenGLESActivity
import com.dastanapps.tettris.TettrisMainActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainUI {
                when (it.id) {
                    1 -> {
                        startActivity(
                            Intent(
                                this@MainActivity,
                                TettrisMainActivity::class.java
                            )
                        )
                    }

//                    2 -> {
//                        startActivity(
//                            Intent(
//                                this@MainActivity,
//                                FruitMergerOpenGLESActivity::class.java
//                            )
//                        )
//                    }

                    4 -> {
                        startActivity(
                            Intent(
                                this@MainActivity,
                                OpenGLESActivity::class.java
                            )
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun MainUI(itemClicked: (GameInfo) -> Unit) {
        GameAppTheme {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                ) {
                    items(
                        arrayOf(
                            GameInfo(1, "Tettris"),
                            GameInfo(2, "Fruit Merging"),
                            GameInfo(3, "2048"),
                            GameInfo(4, "OpenGLES Tutorial"),
                        )
                    ) {
                        Text(
                            text = it.name,
                            modifier = Modifier
                                .padding(vertical = 12.dp)
                                .fillMaxWidth()
                                .clickable {
                                    itemClicked.invoke(it)
                                }
                        )
                    }
                }
            }
        }
    }
}