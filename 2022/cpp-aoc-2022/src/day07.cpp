#include "day07.hpp"

#include <iostream>

struct Command {
    enum class Type { CD, LS };

    friend std::istream& operator>>(std::istream& stream, Command& command);

    Type type;
    std::string target_dir;
};

std::istream& operator>>(std::istream& stream, Command& command) {
    char _;
    std::string command_string;

    stream >> _ >> command_string;

    std::cout << _ << " " << command_string << std::endl;;

    if (command_string == "cd") {
        std::string target_dir;

        stream >> target_dir;

        command.type = Command::Type::CD;
        command.target_dir = target_dir;
    } else if (command_string == "ls") {
        command.type = Command::Type::LS;

        while (stream.peek() != '$') {
            std::string ston;
            std::getline(stream, ston);

            std::cout << ston << std::endl;
        }
    }

    return stream;
}


int day07_part1(std::istream& input) {

    while (input.peek() == '$') {
        Command command;
        input >> command;
    }

    return 123;
}

int day07_part2(std::string input) {
    return 1234;
}
