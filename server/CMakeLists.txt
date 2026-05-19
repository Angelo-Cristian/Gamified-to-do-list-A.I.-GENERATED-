cmake_minimum_required(VERSION 3.15)
project("MobileAppServer")

set(CMAKE_CXX_STANDARD 20)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# Găsim Drogon
find_package(Drogon CONFIG REQUIRED)

# Adăugăm executabilul folosind calea către fișierul tău .cpp
# Observă că am inclus folderul în cale dacă fișierul e acolo
add_executable(MobileAppServer "Mobile App Server/Mobile App Server.cpp" )

# Linkăm librăria
target_link_libraries(MobileAppServer PRIVATE Drogon::Drogon)