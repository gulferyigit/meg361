# INSTRUCTIONS# Proje Özeti

Bu proje *“Günlüğüm”* adında bir Android günlük uygulamasıdır.  

Bu dosya `.github/prompts.md` içeriğine dayanarak, projede uygulanması gereken ekranlar ve özellikler için geliştiricilere adım adım uygulanabilir talimatlar, kabul kriterleri ve önerilen dosya yapısını içerir.Kullanıcılar:

- Günlük yazıları oluşturabilir, görüntüleyebilir, düzenleyebilir ve silebilir.

## Genel Notlar- Her güne bir *duygu (emoji)* ekleyebilir.

- Proje Java/Android (native) olduğundan örnek dosya yolları Android-özgüdür (`app/src/main/java/...`, `app/src/main/res/...`). Ancak talimatlar, tasarım ve iş mantığı açısından kolaylıkla React Native veya Flutter projelerine de uyarlanabilir.- Günlüğe *fotoğraf ekleyebilir*.

- Kod yazarken ViewBinding veya DataBinding kullanımı tercih edilmelidir.# Proje Talimatları (Türkçe)

- Tüm kullanıcı girdilerinde temel doğrulama yapılmalı; hata mesajları kullanıcı dostu olmalı.

- Tema (light/dark) seçimi SharedPreferences içinde saklanmalı.Bu dosya, repodaki `prompts.md` içeriğine dayalı olarak geliştiricilere kısa ve uygulanabilir yönergeler sağlar. Her başlık altında yapılması gerekenleri, kabul kriterlerini ve hızlı kontrol listesini bulabilirsiniz.

- UI tasarımları için Material components kullanılabilir (TextInputLayout, MaterialButton, etc.).

## Genel

---- Proje Java ile Android Gradle projesidir.

- Derleme ve test için Windows PowerShell kullanın.

## 1) Giriş Ekranı (Login Fragment)- Hızlı derleme komutu:

Hedef: E-posta ve şifreyle giriş sağlayan bir Fragment.

```powershell

- Dosya: `app/src/main/java/.../LoginFragment.java` ve layout `app/src/main/res/layout/fragment_login.xml`.\gradlew.bat assembleDebug

- Gereksinimler:```

  - `TextInputLayout` + `TextInputEditText` alanları: email, password.

  - "Giriş Yap" butonu: validasyon sonrası ana ekrana (diary list) geçiş.- Değişiklik yaptıktan sonra her zaman derleyin ve temel ekranları çalıştırarak doğrulayın.

  - "Google ile Giriş Yap" butonu: (opsiyonel) Google Sign-In entegrasyonu placeholder'ı.

  - Kayıt ekranına ilişki: "Kayıt Ol" linki/ butonu ile `RegisterFragment`'e navigation.## Giriş Ekranı

- Kabul Kriteleri:- Fragment oluştur: e-posta ve şifre alanları, "Giriş Yap" butonu.

  - Email formatı kontrolü (ör: `Patterns.EMAIL_ADDRESS`).- "Google ile Giriş Yap" butonu: entegrasyon opsiyoneldir; mock davranış kabul edilir.

  - Şifre boş olmamalı.- Kayıt ekranına geçiş sağlayan bir link/Buton ekleyin.

  - Hatalı girdi durumunda kullanıcıya açıklayıcı hata mesajı gösterilmeli.- Kabul kriterleri:

	- Form doğrulaması (email formatı, minimum şifre uzunluğu) çalışmalı.

## 2) Kayıt Ekranı (Register Fragment)	- Butona basıldığında mock bir giriş akışı tetiklenmeli veya gerçek auth entegre edildiyse çalışmalı.

Hedef: Yeni kullanıcı oluşturma ekranı.

## Kayıt Ekranı

- Dosya: `RegisterFragment.java`, layout `fragment_register.xml`- Fragment: ad, e-posta, şifre alanları.

- Gereksinimler:- Girdi doğrulaması: ad boş olamaz, e-posta formatı, şifre minimum 6 karakter.

  - Alanlar: Ad-Soyad, E-posta, Şifre.- Başarılı kayıt sonrası giriş ekranına dönmeli.

  - Şifre en az 6 karakter olmalı.

  - Başarılı kayıt sonrası `LoginFragment`'e dön.## Günlük Listesi Ekranı

- Kabul Kriterleri:- Fragment: günlük kayıtlarını tarih sırasına göre listeleyen RecyclerView.

  - Girdi doğrulama tüm alanlarda çalışmalı.- Her satır: tarih, emoji, kısa metin önizlemesi.

  - Kayıt akışı bir basit local repository (ör: JSON veya Preferences) ile simüle edilebilmeli.- FloatingActionButton ile yeni günlük ekleme ekranına geçiş.

- Kabul kriterleri:

## 3) Günlük Listesi Ekranı (Diary List)	- RecyclerView düzgün gösteriyor olmalı.

Hedef: Tüm günlüklerin listelendiği ana ekran.	- Yeni günlük eklendiğinde liste güncellenmeli.



- Dosya: `DiaryListFragment.java`, layout `fragment_diary_list.xml`, item layout `item_entry.xml`## Yeni Günlük Ekleme Ekranı

- Gereksinimler:- Fragment: emoji seçimi, metin alanı, fotoğraf ekleme (gallery/camera mock kabul edilir), "Günlüğü Kaydet" butonu.

  - RecyclerView ile tarih sırası (yeni → eski) gösterim.- Kaydetme verisi Room veritabanına eklenmeli.

  - Her satır: emoji, tarih (formatlı), kısa metin (truncate).- Kabul kriterleri:

  - FAB veya buton ile `NewEntryFragment`'e geçiş.	- Veri Room'a kaydediliyor olmalı.

  - Item klik navigasyonu: `EntryDetailFragment`.	- Kaydedilen veri listede görünmeli.

- Kabul Kriterleri:

  - Liste boşsa empty-state göster.## Günlük Detay Ekranı

- Fragment: seçilen günlüğün detaylarını göster (tarih, emoji, metin, fotoğraf).

## 4) Yeni Günlük Ekleme (New Entry Fragment)- Düzenle ve silme işlemleri eklenmeli.

- Dosya: `NewEntryFragment.java`, layout `fragment_new_entry.xml`- Kabul kriterleri:

- Gereksinimler:	- Silme, düzenleme çalışmalı.

  - Emoji seçim alanı (basit: toggleable buttons).

  - Uzun metin için EditText (multi-line).## Profil Ekranı

  - Fotoğraf eklemek için Intent ile kamera/galeri butonu (opsiyonel: placeholder).- Fragment: kullanıcı bilgileri ve uygulama ayarları.

  - Kaydet butonu: Room veya JSON repository'ye kaydet.- Özellikler:

- Kabul Kriterleri:	- İsim düzenleme (TextInputEditText) ve Kaydet butonu.

  - Kaydet sonrası `DiaryListFragment` güncel listeyi gösterir.	- Bildirimler için Switch (SwitchCompat).

	- Tema seçimi (Açık/Koyu) — seçimi SharedPreferences içinde saklayın.

## 5) Günlük Detay Ekranı- Kabul kriterleri:

- Dosya: `EntryDetailFragment.java`, layout `fragment_entry_detail.xml`	- Kaydet butonuna basınca isim saklanmalı ve tekrar yüklendiğinde gösterilmeli.

- Gereksinimler:	- Tema seçimi uygulandığında (uygulamayı yeniden başlatmadan mümkünse) görünüm değişmeli.

  - Tam metin, emoji, tarih, fotoğraf (varsa) göster.

  - Düzenle (edit) ve sil (delete) fonksiyonları.## Tema ve Ayarlar

- Kabul Kriterleri:- Koyu ve açık tema desteklenmeli.

  - Silme onayı (AlertDialog).- Seçilen tema SharedPreferences'ta saklanmalı.

  - Düzenleme sonrası liste güncellenmeli.- Uygulama açıldığında kaydedilmiş tema yüklenmeli.



## 6) Profil Ekranı## Geliştirme İpuçları

- Dosya: `ProfileFragment.java`, layout `fragment_profile.xml`- Stil ve renkleri `res/values/colors.xml` ve `res/values/themes.xml` içinde yönetin.

- Gereksinimler:- Tekrarlayan görünümler için layout include/snippet kullanın (ör. günlük kartı).

  - Kullanıcı adı düzenlenebilir (kaydedilen ad SharedPreferences'e yazılır).- Kodun temiz ve test edilebilir olması için MVVM yaklaşımını takip edin.

  - Bildirim aç/kapa switch.

  - Koyu/açık tema toggle (SharedPreferences ile saklanır, uygulamaya uygulanır).## Test & Hazırlık

- Kabul Kriterleri:- Ana senaryoları manuel olarak test edin:

  - Uygulama yeniden başlatıldığında tema seçimi korunmalı.	- Giriş/Kayıt akışı

	- Günlük ekleme/görüntüleme/detay/düzenle/sil

## 7) Tema & Ayarlar	- Profil düzenleme ve tema kaydı

- Tema seçim mantığı `AppCompatDelegate.setDefaultNightMode(...)` ile uygulanmalı.- CI için basit bir Gradle derleme adımı ekleyin (ör. `assembleDebug`).

- Seçimler `SharedPreferences` içinde tutulmalı.

---

## 8) Veritabanı / RepositoryBu dosyayı ihtiyaçlara göre genişletebilirsiniz. İsterseniz her bölüm için örnek kod snippet'leri, test talimatları veya check-list maddeleri ekleyebilirim.
- Basit JSON repository veya Room kullanılabilir.
- Öneri dosya yapısı:
  - `app/src/main/java/.../model/Entry.java` (id, date, emoji, text, photoUri)
  - `app/src/main/java/.../data/JsonRepository.java` veya `db/EntryDao.java`, `db/EntryDatabase.java` (Room)
  - `app/src/main/java/.../repository/DiaryRepository.java` (arayüz)

## Önerilen Geliştirme Adımları (Sprint-ready)
1. Ortam hazırlık: Projeyi clone et, Gradle sync, emulator/cihaz aç.
2. Authentication (yerel mock) - Login/Register flow implementasyonu.
3. Diary model + repository - persistence (JSON veya Room).
4. Diary list ve new-entry screens.
5. Entry detail + edit/delete.
6. Profile + theme toggling.
7. Görsel ince ayarlar: fontlar, spacing, gradients.
8. Test: unit test for repository, basic instrumentation for navigation.

---

Eğer isterseniz bu INSTRUCTIONS dosyasını daha ayrıntılı (kod snippetleri, örnek repository implementasyonu) hale getirebilirim. Hangi adımı önce yazayım? (örn. Login/Register implementasyonu için tam kod örneği ve file diff hazırlayayım).