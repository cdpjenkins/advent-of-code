cmake_minimum_required(VERSION 3.18)
project(cpp-aoc-2022)

set(CMAKE_CXX_STANDARD 20)

add_executable(aoc
        src/main.cpp
        src/hello.cpp
        src/day01.cpp
        src/util.cpp
)

add_executable(aoc_doctest
        src/hello.cpp
        src/day01.cpp
        src/util.cpp
        test/test.cpp
        test/day01_test.cpp
)
