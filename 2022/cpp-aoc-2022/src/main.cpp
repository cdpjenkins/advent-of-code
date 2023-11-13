#include <iostream>
#include "util.hpp"
#include "day01.hpp"
#include "day02.hpp"

int main(int argc, char* argv[]) {
    std::cout << "Day 01 part 1: ";
    std::cout << day01_part1_elf_with_most_calories(read_input_file("day01.txt")) << std::endl;
    std::cout << "Day 01 part 2: ";
    std::cout << day01_part2_three_elves_with_most_calories(read_input_file("day01.txt")) << std::endl;
    std::cout << std::endl;

    std::cout << "Day 02 part 1: ";
    std::cout << day02_part1_rock_paper_scissors_following_choice(read_input_file("day02.txt")) << std::endl;
    std::cout << "Day 02 part 2: ";
    std::cout << day02_part2_rock_paper_scissors_following_result(read_input_file("day02.txt")) << std::endl;
    std::cout << std::endl;

#ifdef WAIT_FOR_KEYPRESS_BEFORE_QUIT
    std::cout << "Press ENTER to exit" << std::endl;
    std::cin.get();
#endif
}
