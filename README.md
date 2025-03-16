# Location Tracking App

Bu proje, bir Android uygulaması için **location tracking** (konum takibi) özelliğini geliştirmek amacıyla hazırlanmıştır. Uygulama, konum verilerini almak, takip etmek ve kaydetmek için çeşitli işlevlere sahiptir.

## Özellikler

- **Konum Takibi Başlatma ve Durdurma**: Uygulama, kullanıcının cihazındaki verilerini kullanarak konum takibini başlatabilir ve durdurabilir.
- **Konum Verilerini Kaydetme**: Kullanıcıdan alınan konum verileri, veritabanına kaydedilir ve gerektiğinde temizlenebilir.
- **Konum Güncellemeleri**: Gerçek zamanlı olarak konum verileri alınabilir ve uygulama üzerinde görüntülenebilir.

## Teknolojiler

- **Kotlin**: Android uygulama geliştirme için kullanılan modern programlama dili.
- **MVVM**: MVVM tasarım kalıbı kullanılmıştır.
- **Jetpack**: Android uygulama bileşenleri için kütüphaneler.
- **Hilt**: Dependency Injection (DI) için kullanılan framework.

## Kurulum

Bu projeyi kendi bilgisayarınızda çalıştırmak için şu adımları takip edebilirsiniz:

1. GitHub reposunu klonlayın.
2. Android Studio'yu açın ve projeyi yükleyin.
3. Gradle dosyalarını senkronize edin.

## Kullanım

### Konum Takibi Başlatma ve Durdurma
- `startLocationUpdates()` fonksiyonu, konum takibini başlatır.
- `stopLocationUpdates()` fonksiyonu, konum takibini durdurur.

### Konum Kaydetme ve Temizleme
- `clearSavedLocations()` fonksiyonu, tüm kaydedilmiş konum verilerini temizler.
