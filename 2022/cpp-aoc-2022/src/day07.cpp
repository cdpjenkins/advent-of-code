#include "day07.hpp"

#include <iostream>

namespace Day07 {

    struct Command {
        enum class Type { NONE, CD, LS };

        friend std::istream& operator>>(std::istream& stream, Command& command);

        Type type{Type::NONE};
        std::string target_dir;
        std::vector<std::string> file_sizes;
    };

    void skip_whitespace(std::istream& stream) {
        while (std::isspace(stream.peek())) stream.get();
    }

    std::istream& operator>>(std::istream& stream, Command& command) {
        char _;
        std::string command_string;

        stream >> _ >> command_string;

        std::cout << _ << " " << command_string << std::endl;;

        if (command_string == "cd") {
            std::string target_dir;

            stream >> target_dir;

            skip_whitespace(stream);

            command.type = Command::Type::CD;
            command.target_dir = target_dir;
        } else if (command_string == "ls") {
            command.type = Command::Type::LS;
            skip_whitespace(stream);

            while (stream.peek() != '$' && !stream.eof()) {
                std::string ston;
                std::getline(stream, ston);

                std::cout << ston << std::endl;
            }
        }

        return stream;
    }

    int part1(std::istream& input) {

        while (input.peek() == '$') {
            Command command;
            input >> command;
        }

        std::cout << "peek: " << input.peek() << std::endl;

        return 123;
    }

    int part2(std::string input) {
        return 1234;
    }
}
