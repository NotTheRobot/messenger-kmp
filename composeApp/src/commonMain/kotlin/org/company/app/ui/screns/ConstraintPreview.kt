package org.company.app.ui.screns

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.MotionScene
import cafe.adriel.voyager.core.screen.Screen


class ConstraintPreview(): Screen {
    @Composable
    override fun Content() {
        constraintExample()
    }

    @Composable
    fun constraintExample() {
        ConstraintLayout(
            modifier = Modifier.size(500.dp)
        ) {

            val (text1, text2, text3) = createRefs()
            val t2 = remember { mutableStateOf("second") }
            Text(text = "first",
                modifier = Modifier
                    .background(color = Color.Red)
                    .constrainAs(text1) {
                        top.linkTo(parent.top, margin = 100.dp)
                        start.linkTo(parent.start, margin = 150.dp)
                    })

            Text(
                text = t2.value,
                modifier = Modifier
                    .widthIn(max = 400.dp)
                    .background(Color.Cyan)
                    .constrainAs(text2) {
                        top.linkTo(text1.top)
                        start.linkTo(text1.end)
                        end.linkTo(text3.start)
                        width = Dimension.fillToConstraints
                    }
            )

            Text(
                text = "third",
                modifier = Modifier
                    .background(Color.White)
                    .constrainAs(text3) {
                        top.linkTo(text1.top)
                        end.linkTo(parent.end, 150.dp)
                    }

            )

            Button(onClick = {
                t2.value = "ksdjhfsdkjf;lksdjhfkdlsfjsdlkfhk"
            }){
            Box(modifier = Modifier
                .size(64.dp)
                .background(Color.Black))
            }

        }
    }
}
