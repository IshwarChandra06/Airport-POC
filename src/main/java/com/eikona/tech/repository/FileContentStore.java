package com.eikona.tech.repository;

import org.springframework.content.commons.repository.ContentStore;
import org.springframework.stereotype.Component;

import com.eikona.tech.entity.FileSystemContent;


@Component
public interface FileContentStore extends ContentStore<FileSystemContent, String> {
}
