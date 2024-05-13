package com.jk.service;


import com.google.protobuf.Descriptors;
import com.jk.Author;
import com.jk.Book;
import com.jk.BookAuthorServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author Junaid.Khan
 *
 */

@Service
public class BookAuthorClientService {

    @GrpcClient("grpc-jk-service")
    BookAuthorServiceGrpc.BookAuthorServiceBlockingStub sychronousClient;

    @GrpcClient("grpc-jk-service")
    BookAuthorServiceGrpc.BookAuthorServiceStub sychronousClient2;

    public Map<Descriptors.FieldDescriptor,Object> getAuthor(int autherId){
        Author authorRequest = Author.newBuilder()
                .setAuthorId(autherId)
                .build();
       Author authorResponse = sychronousClient.getAuthor(authorRequest);
    return authorResponse.getAllFields();
    }

    public List<Map<Descriptors.FieldDescriptor,Object>> getBooksByAuthor(int authorId) throws InterruptedException {
      final CountDownLatch countDownLatch = new CountDownLatch(1);
        Author authorRequest = Author.newBuilder().setAuthorId(authorId).build();
        final List<Map<Descriptors.FieldDescriptor,Object>> response = new ArrayList<>();
        sychronousClient2.getBooksByAuthor(authorRequest, new StreamObserver<Book>() {
            @Override
            public void onNext(Book book) {
                response.add(book.getAllFields());
            }

            @Override
            public void onError(Throwable throwable) {
                countDownLatch.countDown();
            }

            @Override
            public void onCompleted() {
                countDownLatch.countDown();;
            }
        });

        boolean await = countDownLatch.await(1, TimeUnit.MINUTES);
        return await ? response : Collections.emptyList();
    }

}
