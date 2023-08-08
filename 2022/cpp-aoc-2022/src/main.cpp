#include <iostream>
#include "hello.hpp"

int main(int argc, char* argv[]) {
    std::cout << say_hello("World") << std::endl;

#ifdef WAIT_FOR_KEYPRESS_BEFORE_QUIT
    std::cin.get();
#endif
}

