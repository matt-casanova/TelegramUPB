package com.example.telegramupb
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.telegramupb.ui.theme.TelegramUPBTheme

// El main que llama al scaffold
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //ocoupa t0do el celu
        enableEdgeToEdge()
        setContent {
            TelegramUPBTheme {
                TelegramScaffold()
            }
        }
    }
}

@Composable
fun MinimalDropdownMenu() {
    var expanded by remember { mutableStateOf(false) }
    Box {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(Icons.Default.MoreVert, contentDescription = "More options")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Nuevo grupo") },
                onClick = { expanded = false }
            )
            DropdownMenuItem(
                text = { Text("Ajustes") },
                onClick = { expanded = false }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleSearchBar(
    textFieldState: TextFieldState,
    onSearch: (String) -> Unit,
    searchResults: List<String>,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    SearchBar(
        modifier = modifier,
        inputField = {
            SearchBarDefaults.InputField(
                query = textFieldState.text.toString(),
                onQueryChange = { textFieldState.edit { replace(0, length, it) } },
                onSearch = {
                    onSearch(textFieldState.text.toString())
                    expanded = false
                },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                placeholder = { Text("Buscar en Telegram...") }
            )
        },
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        Column(Modifier.verticalScroll(rememberScrollState())) {
            searchResults.forEach { result ->
                ListItem(
                    headlineContent = { Text(result) },
                    modifier = Modifier
                        .clickable {
                            textFieldState.edit { replace(0, length, result) }
                            expanded = false
                        }
                        .fillMaxWidth()
                )
            }
        }
    }
}

// Clase de datos para representar un chat falso de Telegram
data class MockChat(
    val name: String,
    val lastMessage: String,
    val time: String,
    val unreadCount: Int,
    val avatarColor: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelegramScaffold() {

    var presses by remember { mutableIntStateOf(0) }
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val searchState = remember { TextFieldState() }
    var results by remember { mutableStateOf(listOf<String>()) }

    val tabTitles = listOf("Chats", "Buscar", "Favoritos", "Ajustes")
    val tabIcons = listOf(
        Icons.Default.Home,
        Icons.Default.Search,
        Icons.Default.Favorite,
        Icons.Default.Settings
    )

    // Lista de chats simulados con colores al estilo Telegram
    val mockChats = remember {
        listOf(
            MockChat(
                "Juan Pérez",
                "¡Hola! ¿Lograste compilar el proyecto?",
                "18:42",
                2,
                Color(0xFF517DA2)
            ),
            MockChat(
                "Grupo de Android UPB",
                "Carlos: Recuerden usar Jetpack Compose",
                "17:15",
                14,
                Color(0xFF4CA64C)
            ),
            MockChat("Mamá", "No te olvides de almorzar, hijo.", "14:02", 0, Color(0xFFE57373)),
            MockChat(
                "Canal de Noticias",
                "Último minuto: Lanzamiento de Android 16",
                "Ayer",
                0,
                Color(0xFFF2A743)
            ),
            MockChat(
                "María José",
                "Dale, nos vemos en la universidad a las 4",
                "Ayer",
                1,
                Color(0xFF9575CD)
            ),
            MockChat(
                "Soporte Técnico",
                "Tu código de verificación es 48291",
                "24 sep",
                0,
                Color(0xFF78909C)
            ),
            MockChat(
                "Lucas Silva",
                "Pásame el APK para probarlo porfa",
                "23 sep",
                5,
                Color(0xFF4DB6AC)
            ),
            MockChat(
                "Pochi Bolivar",
                "Chau fish",
                "23 sep",
                532321,
                Color(0xFF4DB3AC)
            ),
            MockChat(
                "Pochi Bolivar",
                "Chau fish",
                "23 sep",
                532321,
                Color(0xFF4DB3AC)
            )
        )
    }

    Scaffold(
        containerColor =Color.White,
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    // Colocamos el buscador directamente ocupando el ancho del título
                    SimpleSearchBar(
                        textFieldState = searchState,
                        searchResults = results,
                        onSearch = { query ->
                            results = listOf(
                                "Resultado para '$query'",
                                "Mensaje antiguo",
                                "Contacto encontrado"
                            )
                        },

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 4.dp)
                    )
                },
                actions = {
                    MinimalDropdownMenu()
                }
            )
        },
        bottomBar = {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.navigationBarsPadding()
            ) {
                tabTitles.forEachIndexed {index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title, fontSize = 12.sp) },
                        icon = {
                            Icon(
                                tabIcons[index],
                                contentDescription = title,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    )
                }
            }
        },
        floatingActionButton = {
            // El clásico botón flotante de Telegram para iniciar un nuevo chat
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                onClick = { presses++ }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo Mensaje")
            }
        }
    ) { innerPadding ->
        // Controlamos qué se muestra en la pantalla según el Tab seleccionado
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTabIndex) {
                0 -> { // PESTAÑA: CHATS (La réplica principal)
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(mockChats) { chat ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { /* No-op */ }
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Nombre y Mensaje (Centro)
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = chat.name,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 16.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = chat.lastMessage,
                                        color = Color.Gray,
                                        fontSize = 14.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                // Hora y Globo de Notificación (Derecha)
                                Column(
                                    horizontalAlignment = Alignment.End,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = chat.time,
                                        color = Color.Gray,
                                        fontSize = 12.sp
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    if (chat.unreadCount > 0) {
                                        Badge(
                                            containerColor = MaterialTheme.colorScheme.primary,
                                            contentColor = MaterialTheme.colorScheme.onPrimary
                                        ) {
                                            Text(text = chat.unreadCount.toString())
                                        }
                                    }
                                }
                                // Avatar circular con la inicial del nombre
                                Spacer(modifier = Modifier.width(16.dp))

                                Box(
                                    modifier = Modifier
                                        .size(52.dp)
                                        .clip(CircleShape)
                                        .background(chat.avatarColor),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = chat.name.take(1),
                                        color = Color.White,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                1 -> { // PESTAÑA: BUSCAR
                    PlaceholderScreen(
                        title = "Pantalla de Búsqueda Global",
                        desc = "Aquí aparecerán tus canales públicos y bots globales."
                    )
                }

                2 -> { // PESTAÑA: FAVORITOS
                    PlaceholderScreen(
                        title = "Mensajes Guardados",
                        desc = "Tus chats e hilos marcados como favoritos se almacenarán en esta sección."
                    )
                }

                3 -> { // PESTAÑA: AJUSTES
                    PlaceholderScreen(
                        title = "Ajustes de la Cuenta",
                        desc = "Configuración de privacidad, notificaciones, temas visuales y datos."
                    )


                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TelegramUPBTheme {
        TelegramScaffold()
    }
}

@Composable
fun PlaceholderScreen(title: String, desc: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = desc,
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}