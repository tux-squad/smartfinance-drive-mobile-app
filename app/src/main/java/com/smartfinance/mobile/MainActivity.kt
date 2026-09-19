package com.smartfinance.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import com.smartfinance.mobile.catalog.presentation.catalog.VehicleListScreen
import com.smartfinance.mobile.catalog.presentation.comparison.VehicleComparisonScreen
import com.smartfinance.mobile.catalog.presentation.concesionarias.ConcesionariasScreen
import com.smartfinance.mobile.catalog.presentation.consulta.ConsultaIAScreen
import com.smartfinance.mobile.catalog.presentation.detail.VehicleDetailScreen
import com.smartfinance.mobile.catalog.presentation.inventory.InventoryScreen
import com.smartfinance.mobile.catalog.presentation.dashboard.dealer.DealershipDashboardScreen
import com.smartfinance.mobile.catalog.presentation.inventory.dealer.AddVehicleScreen
import com.smartfinance.mobile.catalog.presentation.inventory.dealer.DealershipInventoryScreen
import com.smartfinance.mobile.catalog.presentation.testdrive.TestDriveScreen
import com.smartfinance.mobile.core.ui.theme.SmartFinanceDriveTheme
import com.smartfinance.mobile.financing.presentation.preevaluation.PreEvaluationScreen
import com.smartfinance.mobile.financing.presentation.prospects.dealer.DealerProspectsScreen
import com.smartfinance.mobile.financing.presentation.prospects.dealer.ProspectDetailScreen
import com.smartfinance.mobile.financing.presentation.requests.MisSolicitudesScreen
import com.smartfinance.mobile.iam.presentation.login.LoginScreen
import com.smartfinance.mobile.iam.presentation.register.RegisterScreen
import com.smartfinance.mobile.messaging.presentation.messages.MessagesScreen
import com.smartfinance.mobile.messaging.presentation.messages.dealer.DealershipMessagesScreen
import com.smartfinance.mobile.profiles.presentation.profile.ProfileScreen
import com.smartfinance.mobile.settings.presentation.configuration.ConfigurationScreen
import com.smartfinance.mobile.settings.presentation.configuration.dealer.DealershipConfigScreen
import com.smartfinance.mobile.settings.presentation.membership.dealer.DealershipMembershipScreen
import com.smartfinance.mobile.shared.ui.components.AppNavigationDrawer
import com.smartfinance.mobile.shared.ui.components.AppTopHeader
import com.smartfinance.mobile.shared.ui.layouts.DealershipShell
import com.smartfinance.mobile.shared.ui.layouts.MobileShell
import kotlinx.coroutines.launch

// ── App-level navigation states ─────────────────────────────────────────────
private enum class AppScreen { LOGIN, REGISTER, CONSUMER_APP, DEALERSHIP_APP }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartFinanceDriveTheme {
                val application = application as SmartFinanceApplication
                val container = application.container

                // ── Top-level app screen (Login → Consumer app) ───────────
                var appScreen by remember { mutableStateOf(AppScreen.LOGIN) }

                when (appScreen) {

                    // ══════════════════════════════════════════════════════
                    // LOGIN (IAM bounded context)
                    // ══════════════════════════════════════════════════════
                    AppScreen.LOGIN -> LoginScreen(
                        authRepository = container.authRepository,
                        onLoginSuccess = { isDealer ->
                            appScreen = if (isDealer) AppScreen.DEALERSHIP_APP else AppScreen.CONSUMER_APP
                        },
                        onNavigateToRegister = { appScreen = AppScreen.REGISTER }
                    )

                    // ══════════════════════════════════════════════════════
                    // REGISTER (IAM bounded context)
                    // ══════════════════════════════════════════════════════
                    AppScreen.REGISTER -> RegisterScreen(
                        authRepository = container.authRepository,
                        onRegisterSuccess = { appScreen = AppScreen.LOGIN },
                        onNavigateToLogin  = { appScreen = AppScreen.LOGIN }
                    )

                    // ══════════════════════════════════════════════════════
                    // CONSUMER APP (all 13 screens for the buyer)
                    // ══════════════════════════════════════════════════════
                    AppScreen.CONSUMER_APP -> {
                        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                        val scope = rememberCoroutineScope()

                        // ── Bottom-nav tab index ──────────────────────────
                        // 0=Inicio 1=Buscar 2=Mensajes 3=ConsultaIA 4=Reportes 5=MiPerfil
                        var selectedIndex by remember { mutableStateOf(0) }

                        // ── Sub-screen flags inside "Buscar" (tab 1) ─────
                        var isInventoryOpen     by remember { mutableStateOf(false) }
                        var isVehicleDetailOpen by remember { mutableStateOf(false) }
                        var isPreEvaluationOpen by remember { mutableStateOf(false) }
                        var isComparisonOpen    by remember { mutableStateOf(false) }
                        var isTestDriveOpen     by remember { mutableStateOf(false) }

                        // ── Sub-screen flag inside "MiPerfil" (tab 5) ────
                        var isConfigurationOpen by remember { mutableStateOf(false) }

                        // ── Shared vehicle state ──────────────────────────
                        var selectedVehicleName  by remember { mutableStateOf("Toyota RAV4 2024") }
                        var selectedVehiclePrice by remember { mutableStateOf("\$28,500 USD") }
                        var selectedVehicleId by remember { mutableStateOf("1") }

                        // Helper: reset all "Buscar" sub-screens
                        fun resetBuscar() {
                            isInventoryOpen     = false
                            isVehicleDetailOpen = false
                            isPreEvaluationOpen = false
                            isComparisonOpen    = false
                            isTestDriveOpen     = false
                        }

                        // ── Drawer active-route mapping ───────────────────
                        val currentDrawerRoute = when (selectedIndex) {
                            0    -> "dashboard"
                            1    -> "dealerships"
                            2    -> "messages"
                            3    -> "ai_consult"
                            4    -> "reports"
                            5    -> if (isConfigurationOpen) "settings" else "profile"
                            else -> "dashboard"
                        }

                        ModalNavigationDrawer(
                            drawerState = drawerState,
                            drawerContent = {
                                ModalDrawerSheet(drawerContainerColor = Color.Transparent) {
                                    AppNavigationDrawer(
                                        selectedRoute = currentDrawerRoute,
                                        onSelectItem = { routeKey ->
                                            scope.launch { drawerState.close() }
                                            when (routeKey) {
                                                "dashboard", "vehicles" -> {
                                                    selectedIndex = 0; resetBuscar()
                                                }
                                                "dealerships" -> {
                                                    selectedIndex = 1; resetBuscar()
                                                }
                                                "messages"   -> { selectedIndex = 2 }
                                                "ai_consult" -> { selectedIndex = 3 }
                                                "reports"    -> { selectedIndex = 4 }
                                                "profile"    -> {
                                                    selectedIndex = 5
                                                    isConfigurationOpen = false
                                                }
                                                "settings"   -> {
                                                    selectedIndex = 5
                                                    isConfigurationOpen = true
                                                }
                                            }
                                        }
                                    )
                                }
                            }
                        ) {
                            MobileShell(
                                selectedNavIndex = selectedIndex,
                                header = {
                                    AppTopHeader(
                                        showMenuIcon = true,
                                        onMenuClick = {
                                            scope.launch {
                                                if (drawerState.isClosed) drawerState.open()
                                                else drawerState.close()
                                            }
                                        },
                                        onSearchClick = {
                                            selectedIndex = 1
                                            resetBuscar()
                                        },
                                        onProfileClick = {
                                            selectedIndex = 5
                                            resetBuscar()
                                            isConfigurationOpen = false
                                        }
                                    )
                                },
                                onItemClick = { tab ->
                                    selectedIndex = tab
                                    resetBuscar()
                                    if (tab == 5) isConfigurationOpen = false
                                }
                            ) {
                                when (selectedIndex) {

                                    // ── TAB 0: Inicio / Catálogo ──────────
                                    0 -> VehicleListScreen(
                                        vehicleRepository = container.vehicleRepository,
                                        onNavigateToDealerships = {
                                            selectedIndex = 1; resetBuscar()
                                        },
                                        onRequestPreEvaluation = { name, price, id ->
                                            selectedVehicleName  = name
                                            selectedVehiclePrice = price
                                            selectedVehicleId = id
                                            selectedIndex = 1
                                            resetBuscar()
                                            isPreEvaluationOpen = true
                                        }
                                    )

                                    // ── TAB 1: Buscar (sub-screen stack) ──
                                    1 -> when {

                                        // ① Test Drive
                                        isTestDriveOpen -> TestDriveScreen(
                                            vehicleName = selectedVehicleName,
                                            onBack = {
                                                isTestDriveOpen     = false
                                                isVehicleDetailOpen = true
                                            },
                                            onConfirmBooking = { _ ->
                                                resetBuscar()
                                                selectedIndex = 2 // → Mensajes
                                            }
                                        )

                                        // ② Pre-evaluación
                                        isPreEvaluationOpen -> PreEvaluationScreen(
                                            financingRepository = container.financingRepository,
                                            vehicleId       = selectedVehicleId,
                                            vehicleName    = selectedVehicleName,
                                            priceText      = selectedVehiclePrice,
                                            dealershipName = "EuroMotors",
                                            onBack = {
                                                isPreEvaluationOpen = false
                                                isVehicleDetailOpen = true
                                            },
                                            onSubmit = {
                                                resetBuscar()
                                                selectedIndex = 4 // → Mis Solicitudes
                                            }
                                        )

                                        // ③ Comparador
                                        isComparisonOpen -> VehicleComparisonScreen(
                                            onBack = {
                                                isComparisonOpen    = false
                                                isVehicleDetailOpen = true
                                            },
                                            onSelectForEvaluation = { vehicle ->
                                                selectedVehicleName  = vehicle.title
                                                selectedVehiclePrice = vehicle.priceText
                                                isComparisonOpen    = false
                                                isPreEvaluationOpen = true
                                            }
                                        )

                                        // ④ Detalle de vehículo
                                        isVehicleDetailOpen -> VehicleDetailScreen(
                                            vehicleName = selectedVehicleName,
                                            price       = selectedVehiclePrice,
                                            onBack = {
                                                isVehicleDetailOpen = false
                                                isInventoryOpen     = true
                                            },
                                            onRequestPreEvaluation = {
                                                isVehicleDetailOpen = false
                                                isPreEvaluationOpen = true
                                            },
                                            onAddToCompare = {
                                                isVehicleDetailOpen = false
                                                isComparisonOpen    = true
                                            },
                                            onScheduleTestDrive = {
                                                isVehicleDetailOpen = false
                                                isTestDriveOpen     = true
                                            }
                                        )

                                        // ⑤ Inventario
                                        isInventoryOpen -> InventoryScreen(
                                            onOpenDetail = {
                                                isInventoryOpen     = false
                                                isVehicleDetailOpen = true
                                                selectedVehicleName  = "Toyota RAV4 2024"
                                                selectedVehiclePrice = "\$28,500 USD"
                                            }
                                        )

                                        // ⑥ Concesionarias (entrada del tab)
                                        else -> ConcesionariasScreen(
                                            onOpenInventory = { isInventoryOpen = true }
                                        )
                                    }

                                    // ── TAB 2: Mensajes ───────────────────
                                    2 -> MessagesScreen(onConversationClick = {})

                                    // ── TAB 3: Consulta IA ────────────────
                                    3 -> ConsultaIAScreen()

                                    // ── TAB 4: Mis Solicitudes ────────────
                                    4 -> MisSolicitudesScreen(
                                        financingRepository = container.financingRepository
                                    )

                                    // ── TAB 5: Perfil ↔ Configuración ─────
                                    5 -> if (isConfigurationOpen) {
                                        ConfigurationScreen(
                                            onNavigateToProfile = { isConfigurationOpen = false }
                                        )
                                    } else {
                                        ProfileScreen(
                                            profileRepository = container.profileRepository,
                                            financingRepository = container.financingRepository,
                                            userId = container.tokenStorage.getUserId(),
                                            onNavigateToSettings = { isConfigurationOpen = true },
                                            onNavigateToRequests = { selectedIndex = 4 }
                                        )
                                    }

                                    else -> VehicleListScreen(vehicleRepository = container.vehicleRepository)
                                }
                            }
                        }
                    }

                    // ══════════════════════════════════════════════════════
                    // DEALERSHIP APP (Vista del concesionario)
                    // ══════════════════════════════════════════════════════
                    AppScreen.DEALERSHIP_APP -> {
                        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                        val scope = rememberCoroutineScope()

                        // 0=Dashboard, 1=Inventario, 2=Prospectos, 3=Mensajes, 4=Membresía, 5=Configuración
                        var selectedIndex by remember { mutableStateOf(0) } // Default to dashboard
                        var isAddVehicleOpen by remember { mutableStateOf(false) }

                        ModalNavigationDrawer(
                            drawerState = drawerState,
                            drawerContent = {
                                ModalDrawerSheet(drawerContainerColor = Color.Transparent) {
                                    AppNavigationDrawer(
                                        selectedRoute = "dealerships", // Temp fallback
                                        onSelectItem = { scope.launch { drawerState.close() } }
                                    )
                                }
                            }
                        ) {
                            DealershipShell(
                                selectedNavIndex = selectedIndex,
                                header = {
                                    AppTopHeader(
                                        showMenuIcon = true,
                                        onMenuClick = {
                                            scope.launch {
                                                if (drawerState.isClosed) drawerState.open() else drawerState.close()
                                            }
                                        },
                                        onSearchClick = {},
                                        onProfileClick = {}
                                    )
                                },
                                onItemClick = { tab ->
                                    selectedIndex = tab
                                }
                            ) {
                                when (selectedIndex) {
                                    0 -> DealershipDashboardScreen(
                                        onNavigateToInventory = { selectedIndex = 1; isAddVehicleOpen = true },
                                        onNavigateToMessages = { selectedIndex = 3 }
                                    )
                                    1 -> {
                                        if (isAddVehicleOpen) {
                                            AddVehicleScreen(
                                                onCancel = { isAddVehicleOpen = false },
                                                onSave = { isAddVehicleOpen = false }
                                            )
                                        } else {
                                            DealershipInventoryScreen(
                                                onNavigateToAddVehicle = { isAddVehicleOpen = true }
                                            )
                                        }
                                    }
                                    2 -> DealerProspectsScreen() // Prospect Detail moved to internal navigation if needed later
                                    3 -> DealershipMessagesScreen()
                                    4 -> DealershipMembershipScreen()
                                    5 -> DealershipConfigScreen()
                                    else -> {
                                        Box(modifier = androidx.compose.ui.Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                            Text("Pestaña en construcción", color = Color.Gray)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
