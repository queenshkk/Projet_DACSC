cmake_minimum_required(VERSION 3.10)

# Nom du projet
project(ApplicLivre)

# Spécifier le standard C++
set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_STANDARD_REQUIRED True)

# Trouver les modules Qt nécessaires
find_package(Qt5 COMPONENTS Core Gui Widgets REQUIRED)
find_package(Threads REQUIRED)

# Inclure le répertoire ClientBookEncoderQt
include_directories(${PROJECT_SOURCE_DIR}/ClientBookEncoderQt)

# Ajouter l'exécutable
add_executable(ApplicLivre
    ClientBookEncoderQt/main.cpp
    ClientBookEncoderQt/mainwindowclientbookencoder.cpp
    ClientBookEncoderQt/moc_mainwindowclientbookencoder.cpp
)

add_executable(Client
     ClientBookEncoderQt/ClientEncodage.cpp
    ClientBookEncoderQt/TCP.cpp
)

add_executable(Server
    ClientBookEncoderQt/ServerEncodage.cpp
    ClientBookEncoderQt/TCP.cpp
    ClientBookEncoderQt/OBEP.cpp
)

# Lier les bibliothèques Qt nécessaires
target_link_libraries(${PROJECT_NAME} Qt5::Core Qt5::Gui Qt5::Widgets)
target_link_libraries(Server Threads::Threads)