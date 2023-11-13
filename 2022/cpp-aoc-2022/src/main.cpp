#include <iostream>
#include "util.hpp"
#include "day01.hpp"

int main(int argc, char* argv[]) {
    std::cout << "Day 01 part 1: ";
    std::cout << day01_part1_elf_with_most_calories(read_input_file("day01.txt")) << std::endl;
    std::cout << "Day 01 part 2: ";
    std::cout << day01_part2_three_elves_with_most_calories(read_input_file("day01.txt")) << std::endl;

#ifdef WAIT_FOR_KEYPRESS_BEFORE_QUIT
    std::cout << "Press ENTER to exit" << std::endl;
    std::cin.get();
#endif
}
