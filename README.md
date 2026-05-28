# 📱 AccurateUserApp

Aplikasi manajemen data pengguna berbasis Android yang dibangun dengan pendekatan **Offline-First** dan arsitektur **Clean Architecture**. Aplikasi ini memungkinkan pengguna untuk melihat, mencari, memfilter, mengurutkan, dan menambah data user, dengan sinkronisasi otomatis ke server ketika koneksi internet tersedia.

---

## 📋 Daftar Isi

- [Cara Penggunaan Aplikasi](#-cara-penggunaan-aplikasi)
- [Teknologi yang Digunakan](#-teknologi-yang-digunakan)
- [Alasan Desain Tampilan & Interaksi](#-alasan-desain-tampilan--interaksi)
- [Arsitektur Proyek](#-arsitektur-proyek)
- [Struktur Folder](#-struktur-folder)

---

## 🚀 Cara Penggunaan Aplikasi

### Prasyarat
- **Android Studio** Ladybug atau yang lebih baru
- **JDK 17**
- **Android SDK** dengan API Level 24 (minimum) — 36 (compile)
- Koneksi internet untuk fetch data awal dan sinkronisasi (opsional, berkat pendekatan offline-first)

### Langkah Instalasi

1. **Clone repository:**
   ```bash
   git clone https://github.com/Kinn-01/AccurateUserApp.git
   ```

2. **Buka di Android Studio:**
   - File → Open → Pilih folder `AccurateUserApp`

3. **Konfigurasi Firebase:**
   - Pastikan file `google-services.json` sudah terdapat di folder `app/`
   - Jika belum, buat project Firebase baru dan download konfigurasinya

4. **Sync Gradle:**
   - Android Studio akan otomatis menawarkan sync Gradle
   - Atau klik **"Sync Project with Gradle Files"** di toolbar

5. **Jalankan aplikasi:**
   - Pilih device/emulator
   - Klik **Run (▶)** atau tekan `Shift + F10`

### Fitur & Cara Menggunakan

#### 1. Halaman Daftar User (User List Screen)
- **Melihat daftar user:** Saat pertama kali dibuka, aplikasi otomatis memuat data user dari server dan menyimpannya ke database lokal.
- **Pull-to-Refresh:** Tarik layar ke bawah untuk memperbarui data dari server secara manual.
- **Pencarian:** Ketik pada kolom pencarian untuk mencari user berdasarkan nama, email, atau nomor telepon. Hasil filter bersifat real-time.
- **Filter berdasarkan kota:** Tap chip kota pada baris horizontal di bawah kolom pencarian. Pilih "Semua Kota" untuk menampilkan semua user, atau pilih kota tertentu.
- **Urutkan data:** Tap ikon sort (☰) di pojok kanan atas Top App Bar untuk mengurutkan user berdasarkan nama (A-Z, Z-A, atau bawaan/default).
- **Status sinkronisasi:** Setiap kartu user menampilkan badge status:
    - ✅ **"Tersinkronisasi"** — Data sudah berhasil dikirim ke server.
    - ⏳ **"Menunggu sinkronisasi"** — Data tersimpan secara lokal dan akan otomatis dikirim ke server saat koneksi tersedia.

#### 2. Halaman Tambah User (Add User Screen)
- Tap tombol **FAB (+)** di halaman daftar user untuk membuka form.
- Isi semua field yang wajib:
    - **Nama Lengkap** — Minimal harus diisi
    - **Gender** — Pilih salah satu kartu (Laki-laki / Perempuan)
    - **Alamat** — Alamat lengkap user
    - **Kota** — Pilih dari dropdown atau ketik manual (autocomplete tersedia)
    - **Email** — Harus berformat email valid
    - **Nomor Telepon** — Harus berupa angka, panjang 8-15 digit
- Tap tombol **"Simpan User"** untuk menyimpan data.
- Jika validasi gagal, pesan error akan ditampilkan per field.
- Jika berhasil, otomatis kembali ke halaman daftar user.

#### 3. Mekanisme Offline-First
- Data baru **selalu tersimpan di database lokal (Room)** terlebih dahulu.
- Jika ada internet, aplikasi langsung mengirim data ke server.
- Jika **tidak ada internet** atau pengiriman gagal, aplikasi menjadwalkan sinkronisasi ulang menggunakan **WorkManager** yang akan berjalan otomatis ketika koneksi kembali tersedia.

---

## 🛠 Teknologi yang Digunakan

| Teknologi | Versi | Kegunaan |
|---|---|---|
| **Kotlin** | 2.2.10 | Bahasa pemrograman utama |
| **Jetpack Compose** | BOM 2026.02.01 | UI toolkit deklaratif modern untuk membangun antarmuka |
| **Material 3 (Material Design 3)** | (via Compose BOM) | Sistem desain untuk tampilan yang konsisten dan modern |
| **Dagger Hilt** | 2.59.2 | Dependency injection framework |
| **Room Database** | 2.8.4 | Abstraksi SQLite untuk penyimpanan data lokal (offline-first) |
| **Retrofit** | 2.9.0 | HTTP client untuk komunikasi REST API |
| **OkHttp** | 4.12.0 | HTTP client dengan logging interceptor |
| **Moshi** | 1.15.1 | JSON converter/parser untuk serialisasi/deserialisasi data |
| **Kotlin Coroutines** | 1.10.1 | Asynchronous programming & reactive streams (Flow) |
| **WorkManager** | 2.9.0 | Background task scheduler untuk sinkronisasi data offline |
| **Navigation Compose** | 2.8.7 | Navigasi antar screen berbasis Compose |
| **Firebase Analytics** | (via BOM 34.13.0) | Pelacakan event dan perilaku pengguna |
| **KSP (Kotlin Symbol Processing)** | 2.2.10-2.0.2 | Code generation untuk Hilt, Room, dan Moshi |

---

## 🎨 Alasan Desain Tampilan & Interaksi

### Kenapa Menggunakan Jetpack Compose?
Jetpack Compose dipilih sebagai UI toolkit karena merupakan **standar modern Android development** yang direkomendasikan Google. Keuntungannya:
- **Deklaratif** — Kode UI lebih ringkas dan mudah dipahami dibanding XML + View system
- **State-driven rendering** — UI otomatis ter-update saat data berubah melalui `StateFlow` + `collectAsState()`
- **Reusable components** — Komponen seperti `UserCard`, `SearchBar`, `FilterChips`, `SortMenu` dibuat modular dan dapat digunakan kembali di screen lain
- **Terintegrasi langsung** dengan Navigation, Hilt, dan Material 3

### Kenapa Material Design 3?
Material 3 memberikan fondasi desain yang **konsisten, aksesibel, dan adaptif**:
- **Dynamic Color** support — Mendukung light/dark theme secara otomatis mengikuti pengaturan sistem pengguna
- **Elevasi dan shape** yang lebih lembut (RoundedCornerShape 16-20dp) memberikan kesan premium dan modern
- Komponen bawaan seperti `TopAppBar`, `FloatingActionButton`, `FilterChip`, `OutlinedTextField`, dan `PullToRefreshBox` sudah mengikuti accessibility guidelines

### Kenapa Premium Color Palette?
Skema warna tidak menggunakan warna generik (merah, biru standar), melainkan palet warna yang dikurasi:
- **Royal Blue (#1E3A8A)** sebagai primary — Memberikan kesan profesional dan trustworthy
- **Turquoise (#14B8A6)** sebagai secondary/accent — Memberikan kontras segar yang harmonis
- Warna-warna netral menggunakan **Slate palette** untuk readability optimal
- Status colors menggunakan standar industri (hijau = sukses, kuning = warning, merah = error)

### Kenapa Offline-First?
Pendekatan offline-first dipilih karena:
- **User Experience** — Pengguna tidak perlu menunggu koneksi internet untuk menyimpan data
- **Reliabilitas** — Data tidak hilang meskipun koneksi terputus saat submit
- **Sinkronisasi transparan** — Badge status pada kartu user menginformasikan status sinkronisasi dengan jelas (✅ synced / ⏳ waiting)
- **WorkManager** menjamin pengiriman data ke server akan dilakukan secara otomatis dan efisien

### Kenapa Interaksi Seperti Ini?

| Fitur Interaksi | Alasan |
|---|---|
| **Pull-to-Refresh** | Pola interaksi yang familiar bagi pengguna mobile untuk memperbarui data |
| **Search real-time** | Memberikan feedback instan tanpa perlu menekan tombol "cari" |
| **Filter chips horizontal scroll** | Menghemat ruang vertikal sekaligus memberikan akses cepat ke filter kota |
| **Sort dropdown di TopAppBar** | Mengikuti konvensi umum app productivity/data management |
| **Gender selector card** | Lebih visual dan intuitif dibanding radio button tradisional. Menggunakan warna berbeda (biru untuk Laki-laki, turquoise untuk Perempuan) agar mudah dibedakan |
| **City dropdown autocomplete** | Mempermudah pemilihan kota dengan dukungan pencarian ketik manual |
| **Form validation inline** | Error message ditampilkan tepat di bawah field yang bermasalah agar pengguna langsung tahu apa yang harus diperbaiki |
| **Loading indicator** | CircularProgressIndicator ditampilkan saat loading data dan saat submit form, memberikan feedback visual bahwa proses sedang berjalan |
| **Empty state illustration** | Ketika data kosong atau pencarian tidak ditemukan, tampilan empty state yang informatif ditampilkan agar pengguna tidak bingung |
| **User card initials avatar** | Avatar inisial dengan warna gender-coded memberikan identitas visual yang cepat dikenali |

---

## 🏗 Arsitektur Proyek

Aplikasi ini mengimplementasikan **Clean Architecture** dengan 3 lapisan utama:

```
┌─────────────────────────────────────────────────┐
│                PRESENTATION LAYER               │
│  (UI Composables, ViewModels, UI State)         │
│                                                 │
│   UserListScreen ←→ UserListViewModel           │
│   AddUserScreen  ←→ AddUserViewModel            │
│   Components: UserCard, SearchBar, FilterChips  │
├─────────────────────────────────────────────────┤
│                  DOMAIN LAYER                   │
│  (Use Cases, Models, Repository Interface)      │
│                                                 │
│   GetUsersUseCase, CreateUserUseCase            │
│   SearchUsersUseCase, FilterUsersByCityUseCase  │
│   SortUserByNameUseCase                         │
│   User, UserFilter, UserRepository (interface)  │
├─────────────────────────────────────────────────┤
│                   DATA LAYER                    │
│  (Repository Impl, Room, Retrofit, Mapper)      │
│                                                 │
│   UserRepositoryImpl                            │
│   Room: AppDatabase, UserDao, UserEntity        │
│   Retrofit: ApiService, UserDto, CreateUserReq  │
│   UserMapper (Entity ↔ Domain ↔ DTO)            │
│   SyncWorker (WorkManager)                      │
└─────────────────────────────────────────────────┘
```

### Alur Data (Data Flow)

```
User Action → Composable → ViewModel → UseCase → Repository → Data Source
                                                        ↓
                                              ┌─── Room (Local) ──── SQLite
                                              └─── Retrofit (Remote) ── API
```

---

## 📂 Struktur Folder

```
app/src/main/java/com/example/accurateuserapp/
├── AccurateApp.kt                    # Application class (Hilt + WorkManager config)
├── MainActivity.kt                   # Entry point activity (@AndroidEntryPoint)
│
├── data/                             # DATA LAYER
│   ├── local/                        # Local data source
│   │   ├── AppDatabase.kt            # Room database definition
│   │   ├── dao/
│   │   │   └── UserDao.kt            # Data Access Object (queries)
│   │   └── entity/
│   │       └── UserEntity.kt         # Room entity (table schema)
│   ├── remote/                       # Remote data source
│   │   ├── api/
│   │   │   └── ApiService.kt         # Retrofit API interface
│   │   └── dto/
│   │       ├── UserDto.kt            # Data Transfer Object (Moshi)
│   │       ├── CreateUserRequest.kt  # Request body untuk POST
│   │       └── UserListResponse.kt   # Type alias response
│   ├── mapper/
│   │   └── UserMapper.kt            # Mapper: Entity ↔ Domain ↔ DTO
│   ├── repository/
│   │   └── UserRepositoryImpl.kt     # Implementasi repository
│   └── WorkManager/
│       └── SyncWorker.kt            # Background sync worker
│
├── di/                               # DEPENDENCY INJECTION
│   ├── AppModule.kt                  # Provides Context
│   ├── DatabaseModule.kt            # Provides Room DB & DAO
│   ├── NetworkModule.kt             # Provides Moshi, OkHttp, Retrofit, ApiService
│   └── RepositoryModule.kt          # Binds UserRepository interface
│
├── domain/                           # DOMAIN LAYER
│   ├── model/
│   │   ├── User.kt                  # Domain model
│   │   └── UserFilter.kt            # Filter & SortOrder enum
│   ├── repository/
│   │   └── UserRepository.kt        # Repository interface (contract)
│   └── usecase/
│       ├── CreateUserUseCase.kt     # Create user business logic
│       ├── GetUsersUseCase.kt       # Get users with caching
│       ├── SearchUsersUseCase.kt    # Search/filter by query
│       ├── FilterUsersByCityUseCase.kt  # Filter by city
│       └── SortUserByNameUseCase.kt     # Sort by name
│
├── presentation/                     # PRESENTATION LAYER
│   ├── adduser/
│   │   ├── AddUserScreen.kt         # Form tambah user (Composable)
│   │   ├── AddUserViewModel.kt      # ViewModel form
│   │   └── AddUserUiState.kt        # UI state data class
│   ├── components/                   # Reusable UI Components
│   │   ├── UserCard.kt              # Card user dengan avatar & status
│   │   ├── SearchBar.kt             # Search input field
│   │   ├── FilterChips.kt           # City filter chips (LazyRow)
│   │   ├── SortMenu.kt              # Sort dropdown menu
│   │   ├── EmptyState.kt            # Empty/no result state
│   │   └── LoadingIndicator.kt      # Loading spinner
│   ├── navigation/
│   │   ├── AppNavigation.kt         # NavHost & route mapping
│   │   └── Screen.kt                # Screen route definitions
│   └── userlist/
│       ├── UserListScreen.kt        # Daftar user (Composable)
│       ├── UserListViewModel.kt     # ViewModel list
│       └── UserListUiState.kt       # UI state data class
│
├── ui/theme/                         # DESIGN SYSTEM
│   ├── Color.kt                     # Color palette definitions
│   ├── Theme.kt                     # Light/Dark theme config
│   └── Type.kt                      # Typography styles
│
└── util/                             # UTILITIES
    ├── AnalyticsHelper.kt           # Firebase Analytics wrapper
    ├── Extensions.kt                # String validation extensions
    ├── NetworkChecker.kt            # Network connectivity checker
    └── Resource.kt                  # Sealed class (Success/Error/Loading)
```
