# Kepemilikan Modul CampusLostHub

```text
src/id/ac/pens/psdku/campuslosthub/
├── Main.java                         # Intan: entry point dan menu utama
├── contract/                         # Aris: kontrak OOP
│   └── Verifiable.java
├── model/                            # Aris: model domain inti
│   ├── Item.java
│   ├── BarangElektronik.java
│   └── BarangDokumen.java
├── exception/                        # Bilu: custom exception dan error storage
│   ├── ItemNotFoundException.java
│   ├── InvalidClaimException.java
│   └── StorageException.java
├── service/                          # Bilu: logika bisnis dan ArrayList
│   └── LostAndFoundManager.java
├── storage/                          # Bilu: File I/O CSV
│   └── CsvStorage.java
└── view/                             # Intan: UI dan validasi input
    ├── ConsoleView.java
    ├── InputValidator.java
    └── TableFormatter.java
```

## Pembagian role

- **Aris Kurniawan:** `contract/`, `model/`, UML, abstraction, inheritance, dan polymorphism.
- **Sabilu Rosyada (Bilu):** `service/`, `storage/`, `exception/`, `ArrayList<Item>`, CSV, dan exception handling.
- **Intan Kusumaningarum:** `Main.java`, `view/`, `Claimant.java` bila ditempatkan pada `model/`, validasi UI, testing, dan video demo.

## Urutan dependensi

```text
contract/model
      ↓
service/storage/exception
      ↓
view/Main
```

`view` tidak membaca CSV secara langsung. `storage` tidak mengatur tampilan. `service` menggunakan kontrak publik dari `model`.

## Catatan `Claimant`

Sesuai pembagian pada laporan Minggu 1, `Claimant.java` dikerjakan Intan. Secara struktur Java, file tersebut tetap diletakkan di:

```text
model/Claimant.java
```

karena `Claimant` adalah model data, bukan komponen UI.
