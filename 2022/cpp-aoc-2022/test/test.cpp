#define DOCTEST_CONFIG_IMPLEMENT_WITH_MAIN
#include "doctest.h"

#include "../src/hello.hpp"

TEST_CASE("Says hello to you") {
    CHECK(say_hello() == "Hello, World!");
    CHECK(say_hello("Gilbert") == "Hello, Gilbert!");
}
