cmake_minimum_required(VERSION 3.10)

# Definește executabilul și include TOATE fișierele sursă
add_executable(MobileAppServer 
    "Mobile App Server.cpp" 
    "Mobile App Server.h"
)

# Caută Drogon (folosind vcpkg)
find_package(Drogon REQUIRED)

# Leagă proiectul de biblioteca Drogon
target_link_libraries(MobileAppServer PRIVATE Drogon::Drogon)