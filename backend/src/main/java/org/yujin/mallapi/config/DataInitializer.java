package org.yujin.mallapi.config;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.yujin.mallapi.domain.Inquiry;
import org.yujin.mallapi.domain.InquiryStatus;
import org.yujin.mallapi.domain.Member;
import org.yujin.mallapi.domain.MemberRole;
import org.yujin.mallapi.domain.Product;
import org.yujin.mallapi.repository.InquiryRepository;
import org.yujin.mallapi.repository.MemberRepository;
import org.yujin.mallapi.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Component
@RequiredArgsConstructor
@Log4j2
public class DataInitializer implements ApplicationRunner {

    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final InquiryRepository inquiryRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.storage.type:local}")
    private String storageType;

    @Value("${app.storage.local-path:upload}")
    private String uploadPath;

    @Override
    public void run(ApplicationArguments args) {

        log.info("========== 초기 데이터 확인 시작 ==========");

        createMembers();
        createSampleImages();
        createProducts();
        createInquiries();

        log.info("========== 초기 데이터 확인 완료 ==========");
    }

    private void createMembers() {

        long beforeCount = memberRepository.count();

        createMember(
                "user@aaa.com",
                "1111",
                "USER1",
                MemberRole.USER
        );

        createMember(
                "admin@aaa.com",
                "1111",
                "ADMIN",
                MemberRole.USER,
                MemberRole.ADMIN
        );

        createMember(
                "manager@aaa.com",
                "1111",
                "MANAGER",
                MemberRole.USER,
                MemberRole.MANAGER
        );

        long afterCount = memberRepository.count();
        long createdCount = afterCount - beforeCount;

        if (createdCount > 0) {
            log.info("초기 회원 데이터 생성 완료: {}건", createdCount);
        } else {
            log.info("초기 회원 데이터가 이미 존재하여 생성하지 않음");
        }
    }

    private void createMember(
            String email,
            String password,
            String nickname,
            MemberRole... roles) {

        if (memberRepository.existsById(email)) {
            return;
        }

        Member member = Member.builder()
                .email(email)
                .pw(passwordEncoder.encode(password))
                .nickname(nickname)
                .social(false)
                .build();

        for (MemberRole role : roles) {
            member.addRole(role);
        }

        memberRepository.save(member);
    }

    private void createProducts() {

        if (productRepository.count() > 0) {
            log.info("초기 상품 데이터가 이미 존재하여 생성하지 않음");
            return;
        }

        createProduct(
                "베이직 반팔 티셔츠",
                19900,
                "데일리로 입기 좋은 기본 반팔 티셔츠입니다.",
                "sample_01.jpg"
        );

        createProduct(
                "오버핏 후드티",
                45900,
                "넉넉한 핏과 부드러운 착용감의 후드티입니다.",
                "sample_02.jpg"
        );

        createProduct(
                "슬림 데님 팬츠",
                39900,
                "깔끔한 실루엣의 데님 팬츠입니다.",
                "sample_03.jpg"
        );

        createProduct(
                "코튼 와이드 팬츠",
                42900,
                "편안한 착용감의 코튼 와이드 팬츠입니다.",
                "sample_04.jpg"
        );

        createProduct(
                "라이트 윈드브레이커",
                69900,
                "가볍게 걸치기 좋은 바람막이 자켓입니다.",
                "sample_05.jpg"
        );

        createProduct(
                "미니멀 셔츠",
                34900,
                "깔끔한 디자인의 데일리 셔츠입니다.",
                "sample_06.jpg"
        );

        createProduct(
                "스웨트 조거 팬츠",
                37900,
                "운동과 일상 모두에 잘 어울리는 조거 팬츠입니다.",
                "sample_07.jpg"
        );

        createProduct(
                "니트 가디건",
                55900,
                "간절기에 활용하기 좋은 니트 가디건입니다.",
                "sample_08.jpg"
        );

        createProduct(
                "레귤러 치노 팬츠",
                36900,
                "캐주얼하게 입기 좋은 기본 치노 팬츠입니다.",
                "sample_09.jpg"
        );

        createProduct(
                "데일리 맨투맨",
                32900,
                "심플한 로고 포인트의 데일리 맨투맨입니다.",
                "sample_10.jpg"
        );

        createProduct(
                "트레이닝 셋업 상의",
                48900,
                "편안한 활동성을 제공하는 트레이닝 상의입니다.",
                "sample_11.jpg"
        );

        createProduct(
                "트레이닝 셋업 하의",
                42900,
                "트레이닝 상의와 함께 입기 좋은 하의입니다.",
                "sample_12.jpg"
        );

        createProduct(
                "베이직 볼캡",
                19900,
                "어디에나 잘 어울리는 기본 볼캡입니다.",
                "sample_13.jpg"
        );

        createProduct(
                "캔버스 토트백",
                25900,
                "가볍게 들기 좋은 데일리 토트백입니다.",
                "sample_14.jpg"
        );

        createProduct(
                "러닝 스니커즈",
                79900,
                "가벼운 착용감의 러닝 스니커즈입니다.",
                "sample_15.jpg"
        );

        createProduct(
                "클래식 로퍼",
                89900,
                "포멀한 스타일에 어울리는 클래식 로퍼입니다.",
                "sample_16.jpg"
        );

        createProduct(
                "데일리 백팩",
                59900,
                "수납공간이 넉넉한 데일리 백팩입니다.",
                "sample_17.jpg"
        );

        createProduct(
                "울 블렌드 코트",
                129000,
                "겨울철 활용하기 좋은 울 블렌드 코트입니다.",
                "sample_18.jpg"
        );

        createProduct(
                "패딩 베스트",
                89000,
                "가볍고 따뜻한 패딩 베스트입니다.",
                "sample_19.jpg"
        );

        createProduct(
                "스트라이프 긴팔 티셔츠",
                29900,
                "캐주얼한 무드의 스트라이프 긴팔 티셔츠입니다.",
                "sample_20.jpg"
        );

        log.info("초기 상품 데이터 생성 완료: 20건");
    }

    private void createProduct(
            String pname,
            int price,
            String pdesc,
            String imageFileName) {

        Product product = Product.builder()
                .pname(pname)
                .price(price)
                .pdesc(pdesc)
                .delFlag(false)
                .build();

        product.addImageString(imageFileName);

        productRepository.save(product);
    }

    private void createInquiries() {

        if (inquiryRepository.count() > 0) {
            log.info("초기 문의 데이터가 이미 존재하여 생성하지 않음");
            return;
        }

        createInquiry(
                "배송은 얼마나 걸리나요?",
                "주문 후 배송까지 평균적으로 얼마나 걸리는지 궁금합니다.",
                "user@aaa.com",
                InquiryStatus.WAIT,
                null
        );

        createInquiry(
                "상품 사이즈 문의",
                "오버핏 후드티 사이즈가 크게 나온 편인지 궁금합니다.",
                "user@aaa.com",
                InquiryStatus.DONE,
                "해당 상품은 오버핏으로 제작되어 평소 사이즈보다 여유 있게 착용 가능합니다."
        );

        createInquiry(
                "이미지 업로드 테스트 문의",
                "상품 등록 시 이미지가 정상적으로 표시되는지 확인하고 있습니다.",
                "manager@aaa.com",
                InquiryStatus.WAIT,
                null
        );

        createInquiry(
                "반품 가능 여부 문의",
                "상품 수령 후 단순 변심으로 반품 가능한지 알고 싶습니다.",
                "user@aaa.com",
                InquiryStatus.DONE,
                "상품 수령 후 7일 이내 미사용 상태라면 반품 신청이 가능합니다."
        );

        createInquiry(
                "관리자 답변 기능 확인",
                "관리자 계정으로 답변을 등록할 수 있는지 테스트하는 문의입니다.",
                "manager@aaa.com",
                InquiryStatus.WAIT,
                null
        );

        createInquiry(
                "장바구니 수량 변경 문의",
                "장바구니에서 상품 수량 변경이 정상적으로 되는지 확인하고 있습니다.",
                "user@aaa.com",
                InquiryStatus.WAIT,
                null
        );

        createInquiry(
                "카카오 로그인 문의",
                "카카오 로그인 후 회원 정보 수정 페이지로 이동하는지 확인하고 싶습니다.",
                "user@aaa.com",
                InquiryStatus.DONE,
                "신규 소셜 회원은 최초 로그인 후 회원 정보 수정 페이지로 이동합니다."
        );

        createInquiry(
                "상품 검색 기능 문의",
                "상품명으로 검색하는 기능이 지원되는지 궁금합니다.",
                "manager@aaa.com",
                InquiryStatus.WAIT,
                null
        );

        log.info("초기 문의 데이터 생성 완료: 8건");
    }

    private void createInquiry(
            String title,
            String content,
            String writer,
            InquiryStatus status,
            String reply) {

        Inquiry inquiry = Inquiry.builder()
                .title(title)
                .content(content)
                .writer(writer)
                .status(status)
                .reply(reply)
                .build();

        inquiryRepository.save(inquiry);
    }

    private void createSampleImages() {

        if (!"local".equalsIgnoreCase(storageType)) {
            log.info("파일 저장 방식이 local이 아니어서 샘플 이미지 생성을 생략함");
            return;
        }

        try {
            Path uploadRootPath = Paths.get(uploadPath);

            if (!uploadRootPath.isAbsolute()) {
                uploadRootPath = Paths.get(System.getProperty("user.dir")).resolve(uploadPath);
            }

            uploadRootPath = uploadRootPath.toAbsolutePath().normalize();

            Files.createDirectories(uploadRootPath);

            int createdCount = 0;

            createdCount += createImageIfNotExists(uploadRootPath, "default.jpeg", "MALL", "NO IMAGE", 600, 600);
            createdCount += createImageIfNotExists(uploadRootPath, "s_default.jpeg", "MALL", "NO IMAGE", 200, 200);

            for (int i = 1; i <= 20; i++) {
                String number = String.format("%02d", i);

                createdCount += createImageIfNotExists(
                        uploadRootPath,
                        "sample_" + number + ".jpg",
                        "YUJIN MALL",
                        "PRODUCT " + number,
                        600,
                        600
                );

                createdCount += createImageIfNotExists(
                        uploadRootPath,
                        "s_sample_" + number + ".jpg",
                        "YUJIN MALL",
                        "PRODUCT " + number,
                        200,
                        200
                );
            }

            if (createdCount > 0) {
                log.info("초기 샘플 이미지 생성 완료: {}개, 경로: {}", createdCount, uploadRootPath);
            } else {
                log.info("초기 샘플 이미지가 이미 존재하여 생성하지 않음");
            }

        } catch (Exception e) {
            log.warn("초기 샘플 이미지 생성 실패: {}", e.getMessage());
        }
    }

    private int createImageIfNotExists(
            Path uploadRootPath,
            String fileName,
            String title,
            String subtitle,
            int width,
            int height) throws Exception {

        Path imagePath = uploadRootPath.resolve(fileName).normalize();

        if (Files.exists(imagePath)) {
            return 0;
        }

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = image.createGraphics();

        try {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color backgroundColor = getBackgroundColor(fileName);

            g.setColor(backgroundColor);
            g.fillRect(0, 0, width, height);

            g.setColor(new Color(255, 255, 255, 45));
            g.fillOval(-width / 4, -height / 4, width, height);
            g.fillOval(width / 2, height / 2, width, height);

            g.setColor(Color.WHITE);

            Font titleFont = new Font("SansSerif", Font.BOLD, Math.max(width / 14, 18));
            Font subTitleFont = new Font("SansSerif", Font.BOLD, Math.max(width / 10, 22));

            drawCenteredText(g, title, titleFont, width, height / 2 - height / 12);
            drawCenteredText(g, subtitle, subTitleFont, width, height / 2 + height / 12);

        } finally {
            g.dispose();
        }

        ImageIO.write(image, "jpg", imagePath.toFile());

        return 1;
    }

    private void drawCenteredText(
            Graphics2D g,
            String text,
            Font font,
            int width,
            int y) {

        g.setFont(font);

        FontMetrics metrics = g.getFontMetrics(font);

        int x = (width - metrics.stringWidth(text)) / 2;

        g.drawString(text, x, y);
    }

    private Color getBackgroundColor(String fileName) {

        int hash = Math.abs(fileName.hashCode());

        int r = 80 + (hash % 100);
        int g = 90 + ((hash / 10) % 90);
        int b = 120 + ((hash / 100) % 80);

        return new Color(r, g, b);
    }
}