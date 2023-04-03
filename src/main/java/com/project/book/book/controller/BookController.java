package com.project.book.book.controller;

import com.project.book.book.domain.Book;
import com.project.book.book.dto.request.*;
import com.project.book.book.dto.response.BookResponseDto;
import com.project.book.book.service.BookService;
import com.project.book.common.config.jwt.LoginMember;
import com.project.book.member.dto.LoginMemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;


@RestController
@RequiredArgsConstructor
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    // 검색을 통한 책 저장
    @PostMapping("/fromSearch")
    public ResponseEntity saveBookFromSearch(
                                            @LoginMember final LoginMemberDto loginMemberDto,
                                            @RequestBody @Valid final SaveBookFromSearchDto request
    ) {
        Book book = bookService.saveBookFromSearch(loginMemberDto.getOAuth(), request);
        return ResponseEntity.created(
                        URI.create("/book/" + book.getId()))
                .build();
    }

    // 등록된 책 데이터를 통한 책 저장
    @PostMapping("/fromList")
    public ResponseEntity saveBookFromList(
                                            @LoginMember final LoginMemberDto loginMemberDto,
                                            @RequestBody @Valid final SaveBookFromListDto request
    ) {
        Book book = bookService.saveBookFromList(loginMemberDto.getOAuth(), request);

        return new ResponseEntity<>(book.getId(), HttpStatus.OK);

    }

    @PostMapping("/wish")
    public ResponseEntity<?> saveWishBook(
                                            @LoginMember final LoginMemberDto loginMemberDto,
                                            @RequestBody @Valid final BookInfoDto request
    ) {
        if (!request.validate()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return bookService.saveWishBook(loginMemberDto.getOAuth(), request);
    }

    // 전체 책 조회
    @GetMapping("/all")
    public ResponseEntity<?> getAllBooks(
                                        @ModelAttribute final AllBookFilterDto condition,
                                        @ModelAttribute CustomPageRequest customPageRequest) {
        Page<?> allBook = bookService.getAllBook(condition, customPageRequest.toPageable());

        return new ResponseEntity<>(allBook, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDetail(@PathVariable final Long id) {
        BookResponseDto bookResponseDto = bookService.getDetailBook(id);
        return new ResponseEntity<>(bookResponseDto, HttpStatus.ACCEPTED);
    }

    // 인기 검색어 조회
    @GetMapping("/popular")
    public ResponseEntity<?> getPopularKeyword() {
        return bookService.getPopularKeyword();
    }

}

