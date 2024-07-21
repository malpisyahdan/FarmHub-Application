# FarmHub Application

FarmHub adalah aplikasi platform untuk menghubungkan petani dengan UMKM (Usaha Mikro, Kecil, dan Menengah) dalam memfasilitasi transaksi pembelian langsung produk pertanian. Aplikasi ini juga menyediakan fitur pengiriman untuk memastikan produk sampai dengan baik ke tangan UMKM.

## Fitur Utama

- Pendaftaran dan autentikasi pengguna (petani, UMKM dan Admin).
- Manajemen produk: petani dapat mengelola produk mereka, UMKM dapat menelusuri dan memesan produk.
- Sistem pemesanan: UMKM dapat membuat pesanan langsung dari petani.
- Pengiriman: integrasi dengan layanan pengiriman untuk pengaturan pengiriman produk.
- Pembayaran: sistem pembayaran aman.
- Review dan rating: UMKM dapat memberikan ulasan terhadap produk dan petani.

## Teknologi yang Digunakan

- **Backend**: Spring Boot
- **Database**: PostgreSQL
- **Autentikasi**: Spring Security dengan JWT
- **Lombok**: Mempermudah penggunaan Java dengan Lombok annotations untuk mengurangi boilerplate code.

## Instalasi dan Penggunaan

1. **Persyaratan Sistem**
   - Java 17
   - PostgreSQL database
   - Lombok

2. **Clone Repository**
   - Clone repository dari GitHub:

     ```
     git clone https://github.com/malpisyahdan/FarmHub-Application.git
     cd farmhub
     ```

3. **Konfigurasi Database**
   - Buatlah database dengan nama `farmhub`.
   - Run Query "CREATE EXTENSION IF NOT EXISTS "uuid-ossp";"
   - Konfigurasi koneksi database di `application.properties`:

     ```
     spring.application.name=${SPRING_APPLICATION_NAME:farmhub}
     spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5432/farmhub}
     spring.datasource.username=${SPRING_DATASOURCE_USERNAME:postgres}
     spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:postgres}
     spring.datasource.driverClassName=org.postgresql.Driver
     spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
     application.security.jwt.secret-key=${APPLICATION_SECURITY_JWT_SECRET_KEY:8688ef4668947738601b8f5d6754498fa28c7d8178b7a151c912c3b30dd7ed6d}
     application.security.jwt.access-token-expiration=${APPLICATION_SECURITY_JWT_ACCESS_TOKEN_EXPIRATION:3600000}
     
     ```
4. ** Instal Lombok **

5. **Menjalankan Aplikasi**
   - Jalankan aplikasi
  
6. **Dockerize File & Docker Compose**

   - mvn clean package
   - docker build -t nama_image_docker . 
   - docker run -p 8080:8080 nama_image_docker

     or
     
    - mvn clean package
    - docker-compose up --build
  
7. Body Register/Create User for Role FARMER,UMKM and ADMIN
    ```
    - User with role FARMER
        {
          "firstName": "Agus",
          "lastName": "Setyadi",
          "username": "agus",
          "password": "Password123*",
         "role": "FARMER"
        }

    - User with role UMKM
       {
          "firstName": "Rio",
          "lastName": "Dani",
          "username": "rio",
          "password": "Password123*",
          "role": "UMKM"
       }
    
     - User with role ADMIN
       {
          "firstName": "Ahmad",
          "lastName": "Rizky",
          "username": "ahmad",
          "password": "Password123*",
          "role": "ADMIN"
       }
    ```
   
8. **API Documentation**
   - Berikut adalah contoh beberapa endpoint yang tersedia:
     - `POST /api/v1/farmhub/register`: Registrasi pengguna baru.
     - `POST /api/v1/farmhub/login`: Autentikasi dan mendapatkan token JWT.
     - `POST /api/v1/farmhub/product`: Mendapatkan daftar produk.
     - `POST /api/v1/farmhub/order`: Membuat pesanan baru.
    
     Ketika aplikasi sudah berjalan, Untuk API Documentation lebih lengkap bisa di lihat di SWAGGER
     -  http://localhost:8080/swagger-ui/index.html#

Terima kasih telah menggunakan FarmHub!


