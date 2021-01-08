package platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import platform.repos.CodeRepository;
import platform.types.Code;
import platform.types.JSONResponse;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RestController
@SuppressWarnings("unused")
public class Controller {

    private Code code = new Code();

    @Autowired
    CodeRepository codeRepository;

    @GetMapping(path = "/api/code/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public JSONResponse getJSONCode(HttpServletResponse response, @PathVariable UUID id) {

        response.addHeader("Content-type", "application/json");

        Code selectedSnippet = findByUUID(id);

        if (selectedSnippet.isTimeRestricted() && selectedSnippet.isViewRestricted()) {

            selectedSnippet.setTime
                    (selectedSnippet.getTime() - selectedSnippet.getTimeElapsed());

            selectedSnippet.setViews(selectedSnippet.getViews() - 1);

            if (selectedSnippet.getTime() <= 0 || selectedSnippet.getViews() <= 0) {

                selectedSnippet.setHidden(true);

                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        } else if (selectedSnippet.isTimeRestricted()) {

            selectedSnippet.setTime
                    (selectedSnippet.getTime() - selectedSnippet.getTimeElapsed());

            if (selectedSnippet.getTime() <= 0) {
                selectedSnippet.setHidden(true);

                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        } else if (selectedSnippet.isViewRestricted()) {

            selectedSnippet.setViews(selectedSnippet.getViews() - 1);

            if (selectedSnippet.getViews() <= 0) {
                selectedSnippet.setHidden(true);

                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        }

        codeRepository.save(selectedSnippet);

        return selectedSnippet.getAsJSON();
    }

    @GetMapping(path = "/code/{id}")
    public String getHtmlCode(HttpServletResponse response, @PathVariable UUID id) {

        response.addHeader("Content-type", "text/html");

        Code selectedSnippet = findByUUID(id);

        if (selectedSnippet.getViews() == 1) {
            selectedSnippet.setShowViewsAnyway(true);
            selectedSnippet.setViews(selectedSnippet.getViews() - 1);

            return selectedSnippet.getAsHTML();
        }

        if (selectedSnippet.isTimeRestricted() && selectedSnippet.isViewRestricted()) {

            selectedSnippet.setTime
                    (selectedSnippet.getTime() - selectedSnippet.getTimeElapsed());
            selectedSnippet.setViews(selectedSnippet.getViews() - 1);

            if (selectedSnippet.getTime() <= 0 || selectedSnippet.getViews() <= 0) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        } else if (selectedSnippet.isTimeRestricted()) {

            selectedSnippet.setTime
                    (selectedSnippet.getTime() - selectedSnippet.getTimeElapsed());

            if (selectedSnippet.getTime() <= 0) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        } else if (selectedSnippet.isViewRestricted()) {

            selectedSnippet.setViews(selectedSnippet.getViews() - 1);

            if (selectedSnippet.getViews() <= 0) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        }

        codeRepository.save(selectedSnippet);

        return selectedSnippet.getAsHTML();
    }

    @PostMapping(path = "/api/code/new", produces = "application/json")
    public String updateCode(@RequestBody Code newCode) {

        newCode.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
        newCode.setId(Code.generateUUID());

        code = newCode;

        code.setTimeRestricted(code.getTime() > 0);
        code.setViewRestricted(code.getViews() > 0);

        codeRepository.save(code);

        return "{\n\"id\": \"" + code.getId() + "\"\n}";
    }

    @GetMapping(path = "/code/new")
    public String getNewHtml() {
        return code.getNewHTML();
    }

    @GetMapping(path = "api/code/latest")
    public List<JSONResponse> getLatestJSON() {

        codeRepository.findAll().forEach(Code::latestCheckHidden);

        List<Code> codeRepositoryCopy = codeRepository.findAll();
        Collections.reverse(codeRepositoryCopy);

        List<Code> copy = codeRepositoryCopy.stream()
                .filter(Predicate.not(Code::isHidden))
                .limit(10)
                .collect(Collectors.toList());

        copy.forEach(element -> {
            element.setViews(0);
            element.setTime(0);
        });

        return copy.stream()
                .map(Code::getAsJSON)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "code/latest")
    public List<String> getLatestHTML() {

        codeRepository.findAll().forEach(Code::latestCheckHidden);

        List<Code> codeRepositoryCopy = codeRepository.findAll();
        Collections.reverse(codeRepositoryCopy);

        List<Code> copy = codeRepositoryCopy.stream() // selects 10 not-restricted snippets
                .filter(Predicate.not(Code::isHidden))
                .limit(10)
                .collect(Collectors.toList());

        copy.forEach(element -> { // sets time and views to 0 only for test requirements
            element.setViews(0);
            element.setTime(0);
        });

        return copy.stream()
                .map(Code::getAsLatestHTML)
                .collect(Collectors.toList());
    }

    private Code findByUUID(UUID uuid) {
        Code selected = codeRepository.findByuuid(uuid).orElse(null);

        if (selected != null) {
            return selected;
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

}
