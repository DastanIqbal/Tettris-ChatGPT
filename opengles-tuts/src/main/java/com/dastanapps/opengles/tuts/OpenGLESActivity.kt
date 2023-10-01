package com.dastanapps.opengles.tuts

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
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
import com.dastanapps.compose.models.ItemB
import com.dastanapps.games.ui.theme.GameAppTheme
import com.dastanapps.opengles.tuts.airhockey.AirHockeyActivity
import com.dastanapps.opengles.tuts.flappybird.FlappyBirdActivity
import com.dastanapps.opengles.tuts.particles.ParticlesActivity

/**
 *
 * Created by Iqbal Ahmed on 24/09/2023 12:40 AM
 *
 */

class OpenGLESActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainUI {
                when (it.id) {
                    1 -> {
                        startActivity(
                            Intent(
                                this@OpenGLESActivity, AirHockeyActivity::class.java
                            )
                        )
                    }

                    2 -> {
                        startActivity(
                            Intent(
                                this@OpenGLESActivity, ParticlesActivity::class.java
                            )
                        )
                    }

                    3 -> {
                        startActivity(
                            Intent(
                                this@OpenGLESActivity, FlappyBirdActivity::class.java
                            )
                        )
                    }

                    4 -> {
//                        startActivity(
//                            Intent(
//                                this@OpenGLESActivity,
//                                OpenGLESActivity::class.java
//                            )
//                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun MainUI(itemClicked: (ItemB) -> Unit) {
        GameAppTheme {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                ) {
                    items(
                        arrayOf(
                            ItemB(1, "Air Hockey"),
                            ItemB(2, "Particle Effects"),
                            ItemB(3, "Flappy Bird"),
//                            ItemB(2, "Fruit Merging"),
//                            ItemB(2, "Fruit Merging"),
//                            ItemB(3, "2048"),
//                            ItemB(4, "OpenGLES Tutorial"),
                        )
                    ) {
                        Text(text = it.name,
                            modifier = Modifier
                                .padding(vertical = 12.dp)
                                .fillMaxWidth()
                                .clickable {
                                    itemClicked.invoke(it)
                                })
                    }
                }
            }
        }
    }
}